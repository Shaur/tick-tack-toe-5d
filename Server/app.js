//http://expressjs.com/guide.html
//https://github.com/mongodb/node-mongodb-native

var express = require ("express");
var mongo = require ("mongodb");
var mongoClient;

var server = new mongo.Server("127.0.0.1", 27017, {});

function collection (name) {
	return new mongo.Collection(mongoClient, name);
}

function index (req, res) {
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

function getDeviceKey (req, res) {
	var device = req.query.key;
	if (undefined == device) {
		res.send ("");
		return;
	}

	var cursor = collection("users").find({"device": device});
	cursor.nextObject(function (err, doc) {
		if (doc != null) {
			res.send (doc._id.toString());
		} else {
			var id = new mongo.ObjectID();
			collection("users").insert(
				{"_id": id, "device": device},
				{safe: true},
				function (err, objects) {
					res.send (id.toString());
				}
			);
		}
	});
}

function play (req, res) {
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
		res.send (doc.device);
		return;
	});
}

function appInit () {
	var app = express();
	//work
	app.get ("/getDeviceKey", getDeviceKey);
	app.get ("/play", play);


	//test
	app.get ("/", index);
	app.get ("/get", get);
	app.get ("/tryit", tryit);
	app.listen(8080);
}

new mongo.Db ("androidServer", server, {}).open (function (error, client) {
	mongoClient = client;
	appInit();
});