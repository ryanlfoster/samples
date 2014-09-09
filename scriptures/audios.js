var app = angular.module('audios', [ 'ngResource', 'SharedServices' ])
	.factory('Books', function($resource) {
		return $resource('scripture/:scriptureId', { scriptureId : '@scriptureId' }); 
	}).factory('Verses', function($resource){
		return $resource('scripture/:scriptureId/book/:bookId', { scriptureId : '@scriptureId', bookId: '@bookId' });
	});

app.filter('capitalize', function() {
    return function(input, scope) {
        return input.substring(0,1).toUpperCase()+input.substring(1);
    }
});

function audioCtrl($scope, $http, Books, Verses) {
	$scope.lowercase = angular.lowercase;
	$scope.showprogress = false;
	    
	$scope.$on('ChapterSelected', function(event, data) {
		Books.query({scriptureId: data.bookId}, function(books) {
			$scope.books = books;
			delete $scope.chapters;
		});
	});

	$scope.play = function(i){
		var currVerse;
		$scope.currentAudio = $('audio');
		if($scope.audio != $scope.currentAudio[i]) {
			$scope.stop();
			$scope.audio = $scope.currentAudio[i];
			//$scope.audio.load();
		}
		$scope.audio.play();
		$scope.audio.removeEventListener('ended', playNext);
		$scope.audio.removeEventListener('play', highlight);
		$scope.audio.addEventListener('ended', playNext);
		$scope.audio.addEventListener('play', highlight);
		
		function playNext(){
			removeHighlight();
			if($scope.currentAudio.length > i + 1) {
				$scope.play(i + 1);
			}
			if ($scope.verses[i].status == 'SUBMITTED') {
				$scope.approve($scope.verses[i]);
			}
		}
		
		function highlight() {
			var attr = $($scope.audio).attr('id');
			currVerse = $('#verse' + attr);
			var curScrollPos = $("#content").scrollTop(); 
			var curVersePos = currVerse.offset().top;
			
			$("#content").animate({ scrollTop: (curVersePos - 150) + curScrollPos }, "slow");
			console.log( "curScrottPos:" + curScrollPos + " curVersePos:" + curVersePos + " newPos: " + ((200 - curVersePos) + curScrollPos));
			removeHighlight();
			currVerse.addClass('highlight');
		}
		
		function removeHighlight() {
			$('.highlight').removeClass('highlight');
		}
		
	};
	
	$scope.stop = function() {
		$scope.audio && $scope.audio.pause();
	};

	$scope.commentClicked = function(id){
		$scope.stop();
		var comment = $("#comment"+id);
		if (comment.hasClass("hidden")) {
			comment.removeClass("hidden");
		} else {
			comment.addClass("hidden");
		}
	};
	
	$scope.addComment = function(verse) {
		$http({
			method: 'POST',
			url: 'audio/comment/' + verse.id,
			data: verse.comments
		}).success(function(data) {
			$("#comment"+verse.id).addClass("hidden");
			verse.status = "DECLINED";
		});
	};
	
	$scope.approve = function(verse) {
		if (verse.status != "APPROVED") {
			$http({
				method: 'POST',
				url: 'audio/approve/' + verse.id,
			}).success(function(data) {
				verse.status = "APPROVED";
			});
		}
	};

	$scope.decline = function(verse) {
		if (verse.status != "DECLINED") {
			$http({
				method: 'POST',
				url: 'audio/decline/' + verse.id,
			}).success(function(data) {
				verse.status = "DECLINED";
			});
		}
	};

	$scope.loaded = function loaded(id) {
		$scope.loadedCount += 1;
		$( "#progressbar" ).progressbar("option", "value", $scope.loadedCount );

		if ($scope.total <= $scope.loadedCount || $scope.total == 0) {
			$scope.showprogress = false;
			$scope.loadedCount = 0;
			$scope.$apply();
		}
	};
	
	$scope.$on('BookSelected', function(event, data) {
		delete $scope.chapters;
		delete $scope.verses;
		Books.query({scriptureId: data.bookId}, function(books) {
			$scope.books = books;
		});
	});
	
	$scope.$watch('selectedBook', function(newVal, oldVal) {
		if(newVal) {
			delete $scope.chapters;
			delete $scope.verses;
			Books.query({scriptureId: newVal.id}, function(chapters) {
				$scope.chapters = chapters;
			});
		}
	});
	
	$scope.$watch('selectedChapter', function(newVal, oldVal) {
		if(newVal) {
			delete $scope.verses;
			Verses.query({scriptureId: newVal.parent.id, bookId: newVal.id}, function(verses) {
				$("#progressbar").progressbar();
				$("#progressbar").progressbar("option", "max", $scope.total);

				$scope.verses = verses;

				$scope.loadedCount = 0;
				$scope.total = 0;
				$scope.total = verses.length;

				$scope.showprogress = true;
			});
		}
	});
}

angular.module('SharedServices', [])
.config(function ($httpProvider) {
    $httpProvider.responseInterceptors.push('myHttpInterceptor');
    var spinnerFunction = function (data, headersGetter) {
        $('#spinner').show();
        return data;
    };
    $httpProvider.defaults.transformRequest.push(spinnerFunction);
})
//register the interceptor as a service, intercepts ALL angular ajax http calls
.factory('myHttpInterceptor', function ($q, $window) {
    return function (promise) {
        return promise.then(function (response) {
            $('#spinner').hide();
            return response;

        }, function (response) {
            $('#spinner').hide();
            return $q.reject(response);
        });
    };
});