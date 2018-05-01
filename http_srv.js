const http = require('http');
var isJSON = require('is-json');

http.createServer((request, response) => {
  const { headers, method, url } = request;
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
	if (isJSON(body)){
		content = JSON.parse(body)
		console.log("parsed content: " + content['id'])
	}
	response.statusCode = 200;
	body = body + "_server_addon";
	response.setHeader('Content-Type', 'application/json');
	const responseBody = { headers, method, url, body };

    response.write(JSON.stringify(responseBody));
	response.end();

  });
}).listen(3000);