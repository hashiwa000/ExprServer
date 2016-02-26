function doClick() {
  var msg = document.getElementById("send-message").value;
  var url = "rest/calc?exp=" + encodeURIComponent(msg);
  var req = initRequest();
  var callback = createCallback(req);
  doGet(url, req, callback);
  showMsg("");
  showStatus("[Send GET. Now waiting response...]");
}

function initRequest() {
  if (window.XMLHttpRequest) {
    if (navigator.userAgent.indexOf('MSIE') != -1) {
      isIE = true;
    }
    return new XMLHttpRequest();
  } else if (window.ActiveXObject) {
    isIE = true;
    return new ActiveXObject("Microsoft.XMLHTTP");
  }
}

function doGet(url, req, callback) {
    req.open("GET", url, true);
    req.onreadystatechange = callback;
    req.send(null);
}

function createCallback(req) {
    return function () {
        var ok = false;
        if (req.readyState == 4) {
            if (req.status == 200) {
                ok = true;
            }
        }

        if (ok) {
//            var msg = parseMessage(req.responseXML);
            var msg = req.responseText
            showMsg(msg);
            showStatus("[Receive message]");
        } else {
            showStatus("[Request failed : " + req.readyState + "," + req.status + "]");
        }
    }
}

function parseMessage(xml) {
    var pingTag;
    var messageTag;
    var messageStr;

    if (xml==null) return "[received message is null]";

    pingTag = xml.getElementsByTagName("ping")[0];
    if (pingTag==null) return "[ping tag is not found]";

    messageTag = pingTag.getElementsByTagName("message")[0];
    if (messageTag==null) return "[message tag is not found]";

    messageStr = messageTag.childNodes[0].nodeValue;
    if (messageStr==null) return "[message contents is not found]";

    return messageStr;
}

function showMsg(msg) {
    var exp = document.getElementById("send-message").value;
    document.getElementById("receive-message").innerHTML = exp + " = " + msg;
}

function showStatus(msg) {
    document.getElementById("status").innerHTML = msg;
}