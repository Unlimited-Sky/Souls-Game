var websocket;

function initWebsocket(websocketPath)
{
  var websocketURL = "ws://" + window.location.host + websocketPath;
  websocket = new WebSocket(websocketURL);

  websocket.onopen = function(evt) { onOpen(evt) };
  websocket.onclose = function(evt) { onClose(evt) };
  websocket.onmessage = function(evt) { onMessage(evt) };
  websocket.onerror = function(evt) { onError(evt) };
}

function onOpen(evt)
{
  writeToScreen("You have connected.");
}

function onClose(evt)
{
  writeToScreen("You have disconnected.");
}

function onMessage(evt)
{
  var json = jQuery.parseJSON(evt.data);
  var type = json.type;
  var html;

  if(type == "message") {
      html = json.user + ": " + json.message;
  } else {
      html = "ERROR: " + json.message;
  }

  writeToScreen(html);
}

function onError(evt)
{
  writeToScreen("ERROR: " + evt.data);
}

function doSend(message)
{
    var json = new Object();
    json.message = escapeHTML(message);
    websocket.send(JSON.stringify(json));
}

function doLogout() {
    websocket.close();
}

function writeToScreen(html)
{
  var htmlLine = html + "\n";
    $('#gameLog').append(htmlLine);
}

var escape = document.createElement('textarea');

function escapeHTML(html) {
    escape.textContent = html;
    return escape.innerHTML;
}

function doSend(message)
{
    var json = new Object();
    json.message = escapeHTML(message);
    websocket.send(JSON.stringify(json));
}

function doLogout()
{
  websocket.close();
}
