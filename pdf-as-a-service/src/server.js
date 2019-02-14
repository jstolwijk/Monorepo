var http = require("http");
var url = require("url");
const util = require("util");
const exec = util.promisify(require("child_process").exec);
var fs = require("fs");

const fileName = "file.pdf";

http.createServer(server).listen(8081);

async function server(req, res) {
  const parts = url.parse(req.url, true);
  const query = parts.query;

  await generatePdf(query.url);
  writeFile(res);
}

async function generatePdf(url) {
  const { stdout, errors } = await exec(`wkhtmltopdf ${url} ${fileName}`);

  console.log(stdout);
  console.error(errors);
}

async function writeFile(res) {
  var fileStream = fs.createReadStream(fileName);
  fileStream.on("data", function(data) {
    res.write(data);
  });
  fileStream.on("end", function() {
    res.end();
  });
}
