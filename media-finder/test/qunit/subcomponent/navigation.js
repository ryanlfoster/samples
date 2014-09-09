define(['text!subcomponent/navigation.html', 'control'], function(html, control){
    var nav = {
        'view': control.bind(this, html)
    };

    return nav;
});