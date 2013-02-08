//http://expressjs.com/guide.html
//https://github.com/mongodb/node-mongodb-native

var fs = require ('fs');
var gameService = require ('./gameService');
var userService = require ('./userService');
var express = require ("express");
var mongo = require ("mongodb");
var mongoClient;

function collection (name) {
	return new mongo.Collection(mongoClient, name);
}

function appInit () {
	var app = express();
	
	app.get ("/getDeviceKey", function (req, res) {
		userService.getDeviceKey (req, res, function (result) {
			res.send (result);
		});
	});
	app.get ("/play", function (req, res) {
		gameService.play (req, res, function (result) {
			res.send (result);
		});
	});

	app.listen(8080);
}

new mongo.Db ("androidServer", new mongo.Server("127.0.0.1", 27017, {}), {safe: true}).open (function (error, client) {
	mongoClient = client;
	gameService.setup (collection ("users"), collection ("games"));
	userService.setup (collection ("users"));
	appInit();
});