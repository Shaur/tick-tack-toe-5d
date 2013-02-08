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

function appInit () {
	var app = express();
	
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

	app.listen(8080);
}

new mongo.Db ("androidServer", new mongo.Server("127.0.0.1", 27017, {}), {safe: true}).open (function (error, client) {
	mongoClient = client;
	play.setup (collection ("users"), collection ("games"));
	getDeviceKey.setup (collection ("users"));
	appInit();
});