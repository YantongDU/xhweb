//初始化,配置license和证书助手的路径
//在此处更换license
function TopESAConfig() {
    var pathName = window.document.location.pathname;
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    var config = {
        "license": "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",
        "exepath": projectName + "/download/天诚安信数字证书助手-3.6.0.1.exe",
		"disableExeUrl": true
    };
    try {
        TCA.config(config);
    } catch (e) {
        if (e instanceof TCACErr) {
            alert(e.toStr());
			location.href = projectName + "/download/天诚安信数字证书助手-3.6.0.1.exe";
        } else {
            alert("Here is JS Error !!!");
        }
		
		return false;
    }
	return true;
}
