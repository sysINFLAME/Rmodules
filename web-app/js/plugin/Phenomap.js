/**
 * Where everything starts
 * - Register drag and drop.
 * - Clear out all gobal variables and reset them to blank.
 */
function loadPhenomapView(){
    phenoMapView.clear_high_dimensional_input('divCategoryVariable');
    phenoMapView.register_drag_drop();
}

// constructor
var PhenoMapView = function () {
    RmodulesView.call(this);
}

// inherit RmodulesView
PhenoMapView.prototype = new RmodulesView();

// correct the pointer
PhenoMapView.prototype.constructor = PhenoMapView;

PhenoMapView.prototype.submit_job = function (form) {
    var variablesConceptCode = readConceptVariables("divCategoryVariable");

    var formParams = {
        jobType:'phenomap',
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
var phenoMapView = new PhenoMapView();


