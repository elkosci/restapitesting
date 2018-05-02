const http = require('http');
var isJSON = require('is-json');
var url = require('url');

http.createServer((request, response) => {
  const { headers, method, url_request } = request;
  console.log("retrieved headers: " + headers['content-type'])
  let body = [];
  request.on('error', (err) => {
	  console.log("error occured");
    console.error(err);
  }).on('data', (chunk) => {
    body.push(chunk);
  }).on('end', () => {

    body = Buffer.concat(body).toString();
	console.log("BODY: " + body)
	if (method == 'GET'){
        console.log(request.url)
        var my_url = url.parse(request.url, true)
        var query = my_url.query;
        console.log(query)
        if (query['username']){
            body = body + "Hello " + query['username'] ;
        }
        if (query['fullname']){
            body = body + " " + query['fullname'] ;
        }
    }
    if (method == 'POST'){
        if (isJSON(body)){
            content = JSON.parse(body)
            console.log("parsed content: " + content['username'])
            console.log("parsed content: " + content['fullname'])
            body = "Hello " + content['username']
            body = body  + " " + content['fullname']
        }
    }
	response.statusCode = 200;
	response.setHeader('Content-Type', 'application/json');
	const responseBody = { headers, method, url_request, body };

    response.write(JSON.stringify(responseBody));
	response.end();

  });
}).listen(3000);