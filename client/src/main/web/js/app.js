
function Order(id, user, products, cost) {
    var self = this;
    self.id = id;
    self.user = user;
    self.products = products;
    self.cost = cost;
}

function OrdersViewModel() {
    var self = this;

    self.userId = ko.observable();
    self.orders = ko.observableArray([]);

    self.showOrders = function () {
        var userId = self.userId();
        var url = "http://localhost:8080/order-service/order/"+userId
        $.ajax(url, {
            type: "get",
            success: function (allData) {
                for (var index in allData) {
                    var order = allData[index];
                    var orderDetails = new Order(order.id, order.userId, order.products, order.cost);
                    self.orders.push(orderDetails);
                }
            }
        });
    };
}

ko.applyBindings(new OrdersViewModel());