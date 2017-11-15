/**
 * 用户管理
 */
if (!("xh" in window)) {
	window.xh = {};
};
var frist = 0;
var appElement = document.querySelector('[ng-controller=power]');
toastr.options = {
	"debug" : false,
	"newestOnTop" : false,
	"positionClass" : "toast-top-center",
	"closeButton" : true,
	/* 动态效果 */
	"toastClass" : "animated fadeInRight",
	"showDuration" : "300",
	"hideDuration" : "1000",
	/* 消失时间 */
	"timeOut" : "1000",
	"extendedTimeOut" : "1000",
	"showMethod" : "fadeIn",
	"hideMethod" : "fadeOut",
	"progressBar" : true,
};
var zTree;
var setting = {
		view : {
			dblClickExpand : false,
			showLine : true,
			selectedMulti : false
		},
		data : {
			simpleData : {
				enable : true,
				idKey : "id",
				pIdKey : "pId"/*
								 * rootPId : ""
								 */
			}
		},
		callback : {
			beforeClick : function(treeId, treeNode) {
				var zTree = $.fn.zTree.getZTreeObj("tree");
				if (treeNode.isParent) {
					zTree.expandNode(treeNode);
					return false;
				} else {
					// demoIframe.attr("src", treeNode.file + ".html");
					return true;
				}
			}
			/*onClick : TreeOnClick*/
		}
	};
xh.load = function() {
	var app = angular.module("app", []);
	app.config([ '$locationProvider', function($locationProvider) {
		$locationProvider.html5Mode({
			enabled : true,
			requireBase : false
		});
	} ]);
	app.controller("power", function($scope, $http, $location) {
		$scope.securityMenu = true; // 菜单变色
		$scope.userId = $location.search().roleId;
		/* 获取用户权限 
		$http.get("../../web/user/getuserpower?userId="+$scope.userId).success(function(response) {
			$scope.data = response.items;
		});*/
		/* 获取用户菜单权限 */
		$http.get("../../web/menu").success(function(response) {
			var zNodes = response.items;
			console.log(zNodes);
			var t = $("#tree");
			t = $.fn.zTree.init(t, setting, zNodes);
		});
	
		$scope.save=function(){
			$.ajax({
				url : '../../web/user/setuserpower',
				type : 'POST',
				dataType : "json",
				async : false,
				data:{
					formData:xh.serializeJson($("#form").serializeArray()) //将表单序列化为JSON对象
				},
				success : function(data) {
					if (data.result === 1) {
						toastr.success(data.message, '提示');
						xh.refresh();

					} else {
						toastr.error(data.message, '提示');
					}
				},
				error : function(){
				}
			});
		};
		

	});
};
/*$("#selectAll").bind("click", function() {
	var checkVal = [];
	var flag = $(this).is(':checked') ? 1 : 0;
	if ($(this).is(':checked')) {
		$("#form").find("[type='checkbox']").prop("checked", true);// 全选
		
		 * $("[name='tb-check']:checkbox").each(function(){
		 * if($(this).is(':checked')){ checkVal.push($(this).attr("value")); }
		 * });
		 
	} else {
		$("#form").find("[type='checkbox']").prop("checked", false);// 反选
	}
});*/
$("#selectAll").bind("click", function() {
	$("#form").find("[type='checkbox']").prop("checked", true);// 全选
});
$("#selectNo").bind("click", function() {
	$("#form").find("[type='checkbox']").prop("checked", false);// 反选
});
$("#selectOther").bind("click", function() {
	var checkbox=$("#form").find("[type='checkbox']");
	
	for(var i=0;i<checkbox.length;i++){
		if(checkbox[i].checked==true){
			checkbox[i].checked=false;
		}else{
			checkbox[i].checked=true;
		}
	}
	
	/*if($("#form").find("[type='checkbox']").is(':checked')){
		$("#form").find("[type='checkbox']").prop("checked", false);
	}else{
		$("#form").find("[type='checkbox']").prop("checked", true);
	}*/
});
//刷新数据
xh.refresh = function() {
	var $scope = angular.element(appElement).scope();
	// 调用$scope中的方法
	$scope.refresh();

};
