define(["knockout", "text!./home.html"], function(ko, homeTemplate) {

  function HomeViewModel(route) {
  	var self = this;
  	self.users = ko.observableArray([]);
  	self.selectedUser = ko.observable();
    this.message = ko.observable('Welcome to orderService!');
    $.getJSON("http://localhost:8080/api/user-service/user/all", function(data) { 
    	self.users = data;
	});
  }

  HomeViewModel.prototype.doSomething = function() {
    this.message('You invoked doSomething() on the viewmodel.');
  };

  return { viewModel: HomeViewModel, template: homeTemplate };

});
