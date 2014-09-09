define(["Search"], function(Search) {
  test("getSearchCriteria empty", function(){
    var actual = (new Search()).getSearchCriteria();
    var expected = {
      query: "",
      types: []
    };
    deepEqual(actual, expected, "empty forms should result in simple, empty results");
  });

  test("getSearchCriteria non-empty", function(){
    var search = new Search()
    search.view($('<div/>'))

    search._html.find('input[type="checkbox"]').attr('checked', 'checked');
    search._html.find('input[type="text"]').val('Woohoo');

    var actual = search.getSearchCriteria();
    var expected = {
      query: "Woohoo",
      types: ['video', 'images', 'audio', 'documents']
    };
    deepEqual(actual, expected, "empty forms should result in simple, empty results");
  });
});
