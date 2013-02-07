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

function main (req, res, callback) {
	processQuery (req.query.id, function (userId) {
		if (userId == null) {
			callback ("");
			return;
		}
		// TODO: add game or connect game
		callback (userId.toString ());
		return;
	});
}

this.main = main;
this.setup = setup;