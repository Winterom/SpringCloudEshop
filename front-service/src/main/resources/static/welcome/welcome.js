angular.module('market-front').controller('welcomeController', function ($scope, $http) {
    const contextPath = 'http://localhost:5555/recommendation/';
    $scope.loadPopularBuying = function () {
        $http.get(contextPath + 'api/v1/mostbuying/')
            .then(function (response) {
                $scope.mostbuying = response.data;
            });
    };
    $scope.loadPopularAdded = function () {
        $http.get(contextPath + 'api/v1/mostadded/')
            .then(function (response) {
                $scope.mostadded = response.data;
            });
    };
    $scope.loadPopularBuying();
    $scope.loadPopularAdded();
});