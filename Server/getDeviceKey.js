var mongo = require ("mongodb");
var users;

function setup (dbUsers) {
	users = dbUsers;
}

function main (req, res, callback) {
	var device = req.query.key;
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
				{"_id": id, "device": device},
				{safe: true},
				function (err, objects) {
					callback (id.toString ());
				}
			);
		}
	});
}

this.main = main;
this.setup = setup;