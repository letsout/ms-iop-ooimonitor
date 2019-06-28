function GetQueryString(name){
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r){
		return  unescape(r[2]); 
	 }
	 else{
		return null;
	 }
}