angular.module("monitorApp", ["ui.bootstrap"])
    .value("refreshTimer", window.refreshTimer || 1 * 1000 * 60)
    .value("host", window.monitorHost || "http://localhost:8080")
    .factory("monitorConfigMapper",function () {
        return function (config) {
            if (config.items) {
                angular.forEach(config.items, function (item, i) {
                    item.paramkeyValuePairs = [];
                    if (item.params) {
                        angular.forEach(item.params, function (value, key) {
                            item.paramkeyValuePairs.push({key: key, value: value});
                        });
                    }
                });
            }
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
    .controller("monitorCtr", ["$scope", "$timeout", "$filter", "$http", "$window", "refreshTimer",
        "monitorConfigMapper", "host"
        , function ($scope, $timeout, $filter, $http, $window, refreshTimer, monitorConfigMapper, host) {

            var refreshService = function () {
                var getMonitorStatus = function (item) {
                    item.ajaxStatus = "start...";
                    $http.get(host + "/monitor/" + item.id).success(function (data) {
                        $timeout(function () {
                            item.result = data;
                            item.ajaxStatus = "complete";

                        })
                    }).error(function (data, status) {
                            $timeout(function () {
                                item.ajaxStatus = "error " + status;
                            })
                        });
                };

                var getAllMonitorStatus = function () {
                    var monitoring = $scope.vm;
                    angular.forEach(monitoring.items, function (item) {
                        getMonitorStatus(item);
                    });
                };

                var timer = null;
                return {
                    start: function () {
                        getAllMonitorStatus();//first call must be soon.
                        timer = $window.setInterval(getAllMonitorStatus, refreshTimer);
                    },
                    stop: function () {
                        if (timer) {
                            $window.clearInterval(timer);
                        }
                    }
                };
            }();

            $http.get(host + "/monitor/config").success(function (data) {
                $timeout(function () {
                    $scope.vm = monitorConfigMapper(data);
                    refreshService.start();
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

            $scope.getFailedCount = function () {
                if ($scope.vm && $scope.vm.items) {
                    return $filter('filter')($scope.vm.items, {"result.success": false}).length;
                }

                return 0;
            }

        }

    ]);