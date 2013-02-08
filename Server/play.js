var mongo = require ("mongodb");
var users;
var games;
var emptyField = [
	[0, 0, 0, 0, 0],
	[0, 0, 0, 0, 0],
	[0, 0, 0, 0, 0],
	[0, 0, 0, 0, 0],
	[0, 0, 0, 0, 0]
];

function setup (dbUsers, dbGames) {
	users = dbUsers;
	games = dbGames;
}

function processQuery (userId, callback) {
	if (undefined == userId) {
		callback (null);
		return;
	}

	try {
		userId = new mongo.ObjectID(userId);
	} catch (err) {
		callback (null);
		return;
	}

	var cursor = users.find ({"_id": userId});
	cursor.nextObject(function (err, doc) {
		if (doc == null) {
			callback (null);
			return;
		} else {
			callback (doc._id);
			return;
		}
	});
}

function getUserGame (userId, callback) {
	var minDateTime = new Date (new Date () - 30000);

	var cursor = games.find ({"createdBy": userId, "date": {$gte: minDateTime}}); // Сначала ищем игру где юзер создатель игры и не прошло 30 секунд с момента последнего обновления
	cursor.nextObject (function (err, doc) {
		if (doc != null) {
			callback (doc); // Вернуть игру
			return;
		} else {
			var cursor = games.find ({"opponent": userId, "date": {$gte: minDateTime}}); // Затем тоже самое долько для игр где пользователь оппонент
			cursor.nextObject (function (err, doc) {
				if (doc != null) {
					callback (doc); // Вернуть игру
				} else {
					callback (null); // Нет игры =(
				}
			});
		}
	});
}

function createNewGame (userId, callback) {
	var now = new Date ();
	var id = new mongo.ObjectID ();
	games.insert (
		{"_id": id, "date": now, "createdBy": userId, "opponent": "", "field": emptyField, "move": "1"},
		{safe: true},
		function (err, docs) {
			callback (docs[0]);
		}
	);
}

function connectOrCreateGame (userId, callback) {
	var minDateTime = new Date (new Date () - 30000);
	var cursor = games.find({"opponent": "", "date": {$gte: minDateTime}});
	cursor.nextObject (function (err, doc) {
		if (doc == null) {
			createNewGame (userId, callback);
		} else {
			var now = new Date ();
			games.update ({"_id": doc._id}, {$set: {"opponent": userId, "date": now}}, {safe: true}, function (err, docs) {
				getUserGame (userId, callback);
			});
		}
	});
}

function getCurrentGame (userId, callback) {
	getUserGame (userId, function (game) {
		if (game == null) {
			connectOrCreateGame (userId, function (game) {
				callback (game);
			});
		} else {
			callback (game);
		}
	});
}

function main (req, res, callback) {
	processQuery (req.query.id, function (userId) {
		if (userId == null) {
			callback ("");
			return;
		}

		getCurrentGame (userId, function (game) {
			callback (game);
		});
		
		return;
	});
}

this.main = main;
this.setup = setup;