/**
 *@Author Benjamin Baum, Department of Medical Informatics, Göttingen, <benjamin.baum@med.uni-goettingen.de> 
 */
function loadSysinflameOutlierView(){
	sysinflameOutlierView.clear_high_dimensional_input('divVariables');
	sysinflameOutlierView.register_drag_drop();
}

var SysinflameOutlierView = function () {
	RmodulesView.call(this);
}

//inherit RmodulesView
SysinflameOutlierView.prototype = new RmodulesView();

//correct the pointer
SysinflameOutlierView.prototype.constructor = SysinflameOutlierView;

SysinflameOutlierView.prototype.submit_job = function (form) {
	var variables = readConceptVariables("divVariables");
	var jobType = 'sysinflameOutlier'
		var _this = this

		GLOBAL.Binning = false;
	var formParams = {
			jobType: 'sysinflameOutlier',
			variablesConceptPaths:variables
	};

	if(variables == '')
	{
		Ext.Msg.alert('Missing input!', 'Please drag Variables into the variables box.');
		return;
	}
	submitJob(formParams);
}
//analysisOutput

SysinflameOutlierView.printPreview = function(content) {
	var stylesheet = "<html><head><link rel='stylesheet' type='text/css' href='../css/chartservlet.css'></head><body>";
	var generator = window.open('', 'name', 'height=400,width=500, resizable=yes, scrollbars=yes');
	var printbutton = "<input type='button' value=' Print this page 'onclick='window.print();return false;' />";
	var savebutton = "<input type='button' value='Save'  onclick='document.execCommand(\"SaveAs\",null,\".html\")' />";
	generator.document.write(stylesheet + printbutton + content);
	generator.document.close();
}

var sysinflameOutlierView = new SysinflameOutlierView();


