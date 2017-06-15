$.import('sap.hana.xs.lm.core', 'xml2json');
var xml2json = $.sap.hana.xs.lm.core.xml2json.xml2json;

try {

        var query = 'SELECT "SIGNATURE" FROM "8EDD06F21D6243FD92CA9FD2F2FED431"."aicoin.db::SAP_HANA_BLOCKCHAIN.FAULT" WHERE "STATUS" = \'I\'';
        $.trace.debug(query);
        var conn = $.hdb.getConnection();
        var rs = conn.executeQuery(query);
        var client = new $.net.http.Client();
        var l = rs.length;
        conn.close();
        $.response.contentType = "application/json";
        $.response.setBody(JSON.stringify(rs)); 
        
        $.post("ws://aicoin.dyndns.org:20000/wsticker", JSON.stringify(rs),
        	    function(result) { });
} 

catch (e) {
    $.response.status = $.net.http.INTERNAL_SERVER_ERROR;
    $.response.contentType = 'text/plain';
    $.trace.error("Exception raised:" + e.message);
}