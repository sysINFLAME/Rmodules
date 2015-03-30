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
    var variablesMetabolomCode = readConceptVariables("divCategoryVariable");
    var variableCensorEleCode = readConceptVariables("divDataNode");
    
    var metabolomEle = Ext.get("divCategoryVariable");
    var censorEle = Ext.get("divDataNode");
    
    
    
    var formParams = {
        jobType:'sysinflame_diversity',
        variablesMetabolomConceptPaths:variablesMetabolomCode,
        variablesCensorConceptPaths:variableCensorEleCode;
        //        correlationBy:form.correlationBy.value,
  //      correlationType:form.correlationType.value
    };

    //If the list of concepts we are running the analysis on is empty, alert the user.
    if(censorEle.dom.childNodes.length > 1 || metabolomEle.dom.childNodes.length > 1)
    {
        Ext.Msg.alert('Too much input!', 'Please drag only one censor concepts into the variables box.');
        return;
    }

    submitJob(formParams);
}



// init heat map view instance
var sysinflame_DiversityView = new Sysinflame_DiversityView();


