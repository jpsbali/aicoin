$.response.contentType = "text/html";
var body = "";

body += "<html>\n";
body += "<head>\n";
body += "</head>\n";
body += "<body>\n";
body += "<a href=\"fault.xsodata/$metadata\" target=\"meta\">Metadata</a><br />\n";
body += "<a href=\"fault.xsodata/?$format=json\" target=\"sdoc\">Service Doc</a><br />\n";
body += "<a href=\"fault.xsodata/fault/?$top=5&$format=json\" target=\"5fault\">Top 5 Faults</a><br />\n";
body += "<a href=\"fault.xsodata/fault(1)/?$format=json\" target=\"1temp\">First Fault</a><br />\n";
body += "<a href=\"fault.xsodata/fault/?$format=json\" target=\"temps\">All Faults</a><br />\n";
body += "<a href=\"fault.xsodata/fault/?$format=json&$filter=FAULT_STATUS eq 'O'\" target=\"tempsf\">Open Faults</a><br />\n";
body += "<a href=\"fault.xsodata/fault/?$format=json&$filter=FAULT_STATUS eq 'C'\" target=\"tempsf\">Closed Faults</a><br />\n";
body += "<a href=\"fault.xsodata/fault/?$format=json&$filter=FAULT_AIBC_STATUS eq 'I'&$select=FAULT_ID,ASSET_ID,FAULT_TEXT,FAULT_DATE_TIME,FAULT_STATUS\" target=\"tempsnotime\">Open Faults Not Sent to AIBC</a><br />\n";
body += "<a href=\"add_fault.xsjs\" target=\"post\">Add Fault</a><br />\n";
body += "</body>\n";
body += "</html>\n";

$.response.setBody(body);
