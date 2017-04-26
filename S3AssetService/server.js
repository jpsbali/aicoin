/*eslint no-console: 0, no-unused-vars: 0*/
"use strict";

var xsjs  = require("sap-xsjs");
var xsenv = require("sap-xsenv");
var port  = process.env.PORT || 3000;

var options = {
	anonymous : true, // remove to authenticate calls
	redirectUrl : "/index.xsjs"
};

// configure HANA
try {
	options = Object.assign(options, xsenv.getServices({ hana: {tag: "hana"} }));
} catch (err) {
	console.log("[WARN]", err.message);
}

// configure UAA
try {
	options = Object.assign(options, xsenv.getServices({ uaa: {tag: "xsuaa"} }));
} catch (err) {
	console.log("[WARN]", err.message);
}

// start server
xsjs(options).listen(port);

console.log("Server listening on port %d", port);



var fs = require('fs');
var base64 = require('base-64');
var utf8 = require('utf8');

// Load the AWS SDK and UUID
var AWS = require('aws-sdk');
var uuid = require('node-uuid');

// Load express and multer
var express =   require('express');
var multer  =   require('multer');

// Create a bucket and upload something into it
// var bucketName = 'node-sdk-sample-' + uuid.v4();
var bucketName = 'aiblockchainstorage';
var keyName = 'hello_world.txt';

// Create an S3 client
var s3 = new AWS.S3();

var app         =   express();
var storage =   multer.diskStorage({
  destination: function (req, file, callback) {
    callback(null, './uploads');
  },
  filename: function (req, file, callback) {
    keyName = file.fieldname + '-' + Date.now();
    callback(null, keyName);
  }
});

var upload = multer({ storage : storage}).single('userPhoto');

app.get('/',function(req,res){
      res.sendFile(__dirname + "/index.html");
});

app.post('/api/photo',function(req,res){
    upload(req,res,function(err) {
        if(err) {
            return res.end("Error uploading file.");
        }
        res.end("File is uploaded");

        var contents = fs.readFileSync("./uploads/"+keyName);
        var encoded = new Buffer(contents, 'binary').toString('base64');
        console.log(encoded);

        keyName += ".txt";
        var params = {Bucket: bucketName, Key: keyName, Body: encoded};
        s3.putObject(params, function(err, data) {
        if (err)
            console.log(err)
        else
            console.log("Successfully uploaded data to " + bucketName + "/" + keyName);
});

    });
});

app.listen(3000,function(){
    console.log("Working on port 3000");
});
