//http://expressjs.com/guide.html
//https://github.com/mongodb/node-mongodb-native

var fs = require ('fs');
var play = require ('./play');
var getDeviceKey = require ('./getDeviceKey');
var express = require ("express");
var mongo = require ("mongodb");
var mongoClient;

function collection (name) {
	return new mongo.Collection(mongoClient, name);
}

/*function index (req, res) {
	collection("log").insert(
		{"dateTime": new Date ()},
		{safe: true},
		function (err, objects) {
			res.send ("Logged");
		}
	);
}

function get (req, res) {
	var cursor = collection("log").find();
	cursor.sort([["dateTime", "desc"]]).limit(1);
	cursor.nextObject(function (err, doc) {
		if (doc != null)
			var lastDate = new Date (doc.dateTime);
		else
			var lastDate = new Date ();
		res.send (JSON.stringify ({
			"lastDate": lastDate,
			"lastDateSeconds": lastDate.getSeconds()
		}));
	});
}

function tryit (req, res) {
	var cursor = collection("tryit").find();
	cursor.nextObject(function (err, doc) {
		if (doc == null) {
			res.send (" - - - ");
		} else {
			res.send (" + + + ");
		}
	});
}
*/

/*function findActiveGame (callback) {
	var now = new Date (new Date () - 60000);
	var cursor = collection("games").find({"opponent": "", "date": {$gte: now}});
	cursor.nextObject (function (err, doc) {
		if (doc == null) {
			callback (null, null);
		} else {
			callback (doc._id, doc);
		}
	});
}

function createNewGame (createdBy, callback) {
	var now = new Date ();
	var id = new mongo.ObjectID();
	collection("games").insert(
		{"_id": id, "date": now, "createdBy": createdBy, "opponent": "", "field": emptyField, "move": "1"},
		{safe: true},
		function (err, objects) {
			callback (id);
		}
	);
}

function gameStatus (gameId, userId, callback) {
	var now = new Date (new Date () - 60000);
	var cursor = collection("games").find({"_id": gameId});
	cursor.nextObject(function (err, doc) {
		if (doc == null) {
			callback ({"status": "error"});
		} else {
			if (doc.opponent == "") {
				callback ({"status": "waitOpponent"});
			} else {
				if (doc.date < now) {
					callback ({"status": "timeIsOver"});
				} else if ((doc.move == "1" && userId == doc.createdBy) || (doc.move == "2" && userId == doc.opponent)) {
					callback ({"status": "play", "field": doc.field});
				} else {
					callback ({"status": "wait"});
				}
			}
		}
	});
}

function changeGameStatus (gameId, opponent, callback) {
	var now = new Date ();
	collection("games").update({"_id": gameId}, {$set: {"opponent": opponent, "date": now}}, {safe: true}, function () {
		callback ();
	});
}

function playOld (req, res) {
	var id = req.query.id;
	if (undefined == id) {
		res.send ("");
		return;
	}

	try {
		id = new mongo.ObjectID(id);
	} catch (err) {
		res.send ("");
		return;
	}

	var cursor = collection("users").find({"_id": id});
	cursor.nextObject(function (err, doc) {
		if (doc == null) {
			res.send ("");
			return;
		}
		//res.send (doc.device);
		findActiveGame (function (gameId, gameInfo) {
			if (gameId == null) {
				createNewGame (id, function (gameId) {
					gameStatus (gameId, id, function (result) {
						res.send (result);
					});
				});
			} else {
				if (gameInfo.createdBy.toString () == id.toString ()) {
					gameStatus (gameId, id, function (result) {
						res.send (result);
					});
				} else {
					changeGameStatus (gameId, id, function () {
						gameStatus (gameId, id, function (result) {
							res.send (result);
						});
					});
				}
			}
		});
		return;
	});
}
*/



function appInit () {
	var app = express();
	//work
	app.get ("/getDeviceKey", function (req, res) {
		getDeviceKey.main (req, res, function (result) {
			res.send (result);
		});
	});
	app.get ("/play", function (req, res) {
		play.main (req, res, function (result) {
			res.send (result);
		});
	});

	//test
	//app.get ("/", index);
	//app.get ("/get", get);
	//app.get ("/tryit", tryit);
	app.listen(8080);
}

new mongo.Db ("androidServer", new mongo.Server("127.0.0.1", 27017, {}), {safe: true}).open (function (error, client) {
	mongoClient = client;
	play.setup (collection ("users"), collection ("games"));
	getDeviceKey.setup (collection ("users"));
	appInit();
});