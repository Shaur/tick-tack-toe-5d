var mongo = require ("mongodb");
var users;

function setup (dbUsers) {
	users = dbUsers;
}

//TODO: Дублирование кода из gameService
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

function getRandomUserName () {
	return "User_" + Math.floor (Math.random () * 9999999);
}

function getDeviceKey (deviceId, callback) {
	var device = deviceId;
	if (undefined == device) {
		callback ("");
		return;
	}

	var cursor = users.find ({"device": device});
	cursor.nextObject(function (err, doc) {
		if (doc != null) {
			callback (doc._id.toString ());
		} else {
			var id = new mongo.ObjectID();
			users.insert(
				{"_id": id, "device": device, "name": getRandomUserName ()},
				{safe: true},
				function (err, objects) {
					callback (id.toString ());
				}
			);
		}
	});
}

function getSetName (id, name, callback) {
	processQuery (id, function (userId) {
		if (userId == null) {
			callback ("");
			return;
		}

		if (undefined == name) {
			var cursor = users.find ({"_id": userId});
			cursor.nextObject(function (err, doc) {
				callback (doc.name);
			});
		} else {
			var cursor = users.find ({"name": name});
			cursor.nextObject(function (err, doc) {
				if (doc == null) {
					users.update ({"_id": userId}, {$set: {"name": name}}, {safe: true}, function () {
						callback (name);
					});
				} else {
					callback (""); // Не уникально
				}
			});
		}
	});
}

this.getSetName = getSetName;
this.getDeviceKey = getDeviceKey;
this.setup = setup;