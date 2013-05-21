angular.module("monitorApp", ["ui.bootstrap"])
    .factory("monitorConfigMapper",function () {
        return function (config) {

            angular.forEach(config.items, function (item, i) {
                item.paramkeyValuePairs = [];
                angular.forEach(item.params, function (value, key) {
                    item.paramkeyValuePairs.push({key: key, value: value});
                })
            });
            return config;
        };
    }).filter("simpleDescription",function () {
        return function (input, maxlendth) {
            if (input && input.length > maxlendth) {
                return input.substr(0, maxlendth) + "...";
            }
            return input;
        };
    }).filter("timeParser",function () {
        return function (time) {
            var minutes = Math.floor(time / ( 60 * 1000));
            var divisor_for_seconds = time % ( 60 * 1000);
            var seconds = Math.floor(divisor_for_seconds / (1000));
            var divisor_for_mseconds = divisor_for_seconds % 1000;
            var mseconds = Math.ceil(divisor_for_mseconds);

            return minutes + "min:" + seconds + "s:" + mseconds + "ms";
        };
    }).filter("status", function () {
        return function (status) {
            return status ? "Passed" : "Failed";
        };
    })
    .controller("monitorCtr", ["$scope", "$timeout", "$http", "monitorConfigMapper"
        , function ($scope, $timeout, $http, monitorConfigMapper) {

            $http.get("http://localhost:8080/monitor/config").success(function (data) {
                $timeout(function () {
                    $scope.vm = monitorConfigMapper(data);
                    getAllMonitorStatus($scope.vm);
                });
            }).error(function () {
                    console.log(arguments);
                });

            $scope.close = function (item) {
                item.shouldBeOpen = false;
                console.log(item, 1111);
            };

            $scope.open = function (item) {
                item.shouldBeOpen = true;
            };

            var getMonitorStatus = function (item) {
                $http.get("http://localhost:8080/monitor/" + item.id).success(function (data) {
                    item.result = data;
                }).error(function () {
                        console.log(arguments);
                    });
            };

            var getAllMonitorStatus = function (monitoring) {
                angular.forEach(monitoring.items, function (item) {
                    getMonitorStatus(item);
                });
            };
        }]);