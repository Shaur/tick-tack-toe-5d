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

function getWinner (field) {
	var result = checkLine (field, true);
	if (result != 0)
		return result;

	result = checkLine (field, false);
	if (result != 0)
		return result;

	for (var i = -2; i < 3; i++) {
		result = checkDiagonal (field, true, i);
		if (result != 0)
			return result;

		result = checkDiagonal (field, false, i);
		if (result != 0)
			return result;
	}
	
	return 0;
}

function checkDiagonal (field, isMain, offset) {
	var count = 0;
	var lastValue = 0;

	for (var i = 0; i < 5; i++) {
		if (isMain) {
			if (((i + offset) > 4) || ((i + offset) < 0))
				continue;
			var value = field[i + offset][i];
		} else {
			if (((4 - i + offset) > 4) || ((4 - i + offset) < 0))
				continue;
			var value = field[4 - i + offset][i];
		}

		if (value == 0) {
			count = 0;
			lastValue = 0;
		} else {
			if (lastValue != value) {
				count = 1;
				lastValue = value;
			} else {
				count++;
				if (count > 2) {
					return value;
				}
			}
		}

	}
	return 0;
}

function checkLine (field, isRow) {
	for (var i = 0; i < 5; i++) {
		var count = 0;
		var lastValue = 0;
		for (var j = 0; j < 5; j++) {
			if (isRow)
				var value = field[i][j];
			else
				var value = field[j][i];
			if (value == 0) {
				count = 0;
				lastValue = 0;
			} else {
				if (lastValue != value) {
					count = 1;
					lastValue = value;
				} else {
					count++;
					if (count > 2) {
						return value;
					}
				}
			}
		}
	}
	return 0;
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
		{"_id": id, "date": now, "createdBy": userId, "opponent": "", "field": emptyField, "move": "1", "winner": "0"},
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

function getGameByUser (id, callback) {
	processQuery (id, function (userId) {
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

function updateGame (game, x, y, val, move, callback) {
	var now = new Date ();
	var field = game.field;
	if (field[x][y] == 0) {
		field[x][y] = val;
	}
	
	if (winner == 0) {
		winner = getWinner (field);
	}

	games.update ({"_id": game._id}, {$set: {"move": move, "date": now, "field": field, "winner": winner}}, {safe: true}, function (err, docs) {
		callback ();
	});
}

function makeMove (id, x, y, callback) {
	processQuery (id, function (userId) {
		if (userId == null) {
			callback ("");
			return;
		}

		if (undefined == x || undefined == y) {
			callback ("");
			return;
		}

		getCurrentGame (userId, function (game) {
			if (game.createdBy.toString () == userId.toString () && game.move == "1") {
				updateGame (game, parseInt (x), parseInt (y), 1, "2", function () {
					callback ("true");
				});
			} else if (game.opponent.toString () == userId.toString () && game.move == "2") {
				updateGame (game, parseInt (x), parseInt (y), 2, "1", function () {
					callback ("true");					
				});
			} else {
				callback ("false");
				return;
			}
		});

		return;
	});
}

this.makeMove = makeMove;
this.getGameByUser = getGameByUser;
this.setup = setup;