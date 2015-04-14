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
    var variablesMicrobiomCode = readConceptVariables("divDataNode");
    var variableCensorEleCode = readConceptVariables("divCategoryVariable");
    var variablechkGroupIndex = form.correlationType.value;
    console.log("ture? " + form.correlationType.value);
    var metabolomEle = Ext.get("divDataNode");
    var censorEle = Ext.get("divCategoryVariable");
    var rnaseqVal = variablesMicrobiomCode;
    var jobType = 'sysinflameDiversity'
    var _this = this
    
	GLOBAL.Binning = false;
    var formParams = {
    	jobType: 'sysinflameDiversity',
        variablesMicrobiomConceptPaths:variablesMicrobiomCode,
        variablesCensorConceptPaths:variableCensorEleCode,
        chkGroupIndex:variablechkGroupIndex
    };
    
    
    if(variablesMicrobiomCode == '')
    {
        Ext.Msg.alert('Missing input!', 'Please drag Microbiome-Data into the variables box.');
        return;
    }
    else if (variableCensorEleCode == ''){
    	 Ext.Msg.alert('Missing input!', 'Please drag categorical variables into the variables box.');
         return;
    }
    //If the list of concepts we are running the analysis on is empty, alert the user.
//    if(censorEle.dom.childNodes.length > 1 || metabolomEle.dom.childNodes.length > 1)
//    {
//        Ext.Msg.alert('Too much input!', 'Please drag only one censor concepts into the variables box.');
//        return;
//    }
//    if(censorEle.dom.childNodes.length < 1 || metabolomEle.dom.childNodes.length < 1)
//    {
//        Ext.Msg.alert('Missing input!', 'Please drag one censor concepts into each variable box.');
//        return;
//    }
console.warn('submitjob@JS')
    submitJob(formParams);
}



// init heat map view instance
var sysinflameDiversityView = new SysinflameDiversityView();


