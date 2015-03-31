/**
 * Where everything starts
 * - Register drag and drop.
 * - Clear out all gobal variables and reset them to blank.
 */
function loadSysinflameDiversityView(){
	sysinflameDiversityView.clear_high_dimensional_input('divCategoryVariable');
	sysinflameDiversityView.register_drag_drop();
}

// constructor
var SysinflameDiversityView = function () {
    RmodulesView.call(this);
}

// inherit RmodulesView
SysinflameDiversityView.prototype = new RmodulesView();

// correct the pointer
SysinflameDiversityView.prototype.constructor = SysinflameDiversityView;

SysinflameDiversityView.prototype.submit_job = function (form) {
    var variablesMetabolomCode = readConceptVariables("divCategoryVariable");
    var variableCensorEleCode = readConceptVariables("divDataNode");
    
    var metabolomEle = Ext.get("divCategoryVariable");
    var censorEle = Ext.get("divDataNode");
    var rnaseqVal = variablesMetabolomCode;
    var jobType = 'sysinflameDiversity'
    var _this = this
    
    var formParams = {
        variablesMetabolomConceptPaths:variablesMetabolomCode,
        variablesCensorConceptPaths:variableCensorEleCode,
        analysisConstraints: JSON.stringify({
            "job_type": _this.jobType,
            "data_type": "rnaseq",
            "assayConstraints": {
                "patient_set": [GLOBAL.CurrentSubsetIDs[1], GLOBAL.CurrentSubsetIDs[2]],
                "assay_id_list": null,
                "ontology_term": [
                    {
                        'term': rnaseqVal,
                        'options': {'type': "default"}
                    }
                ],
                "trial_name": null
            },
            "dataConstraints": {
                "disjunction": null
            },
            "projections": ["rnaseq_values"]
        }),
        jobType: _this.jobType
    };

    //If the list of concepts we are running the analysis on is empty, alert the user.
    if(censorEle.dom.childNodes.length > 1 || metabolomEle.dom.childNodes.length > 1)
    {
        Ext.Msg.alert('Too much input!', 'Please drag only one censor concepts into the variables box.');
        return;
    }
    if(censorEle.dom.childNodes.length < 1 || metabolomEle.dom.childNodes.length < 1)
    {
        Ext.Msg.alert('Missing input!', 'Please drag one censor concepts into each variable box.');
        return;
    }
console.warn('submitjob@JS')
    submitJob(formParams);
}



// init heat map view instance
var sysinflameDiversityView = new SysinflameDiversityView();


