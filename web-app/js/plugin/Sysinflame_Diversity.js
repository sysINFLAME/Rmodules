/**
 * Where everything starts
 * - Register drag and drop.
 * - Clear out all gobal variables and reset them to blank.
 */
function loadSysinflame_diversityView(){
	sysinflame_DiversityView.clear_high_dimensional_input('divCategoryVariable');
	sysinflame_DiversityView.register_drag_drop();
}

// constructor
var Sysinflame_DiversityView = function () {
    RmodulesView.call(this);
}

// inherit RmodulesView
Sysinflame_DiversityView.prototype = new RmodulesView();

// correct the pointer
Sysinflame_DiversityView.prototype.constructor = Sysinflame_DiversityView;

Sysinflame_DiversityView.prototype.submit_job = function (form) {
    var variablesConceptCode = readConceptVariables("divCategoryVariable");

    var formParams = {
        jobType:'sysinflame_diversity',
        variablesConceptPaths:variablesConceptCode
//        correlationBy:form.correlationBy.value,
  //      correlationType:form.correlationType.value
    };

    var variableEle = Ext.get("divCategoryVariable");

    //If the list of concepts we are running the analysis on is empty, alert the user.
    if(variablesConceptCode == '' || (variableEle.dom.childNodes.length < 2))
    {
        Ext.Msg.alert('Missing input!', 'Please drag at least two concepts into the variables box.');
        return;
    }

    submitJob(formParams);
}



// init heat map view instance
var sysinflame_DiversityView = new Sysinflame_DiversityView();


