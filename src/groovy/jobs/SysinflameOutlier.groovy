package jobs

import jobs.steps.*
import jobs.steps.helpers.CategoricalColumnConfigurator;
import jobs.steps.helpers.ColumnConfigurator
import jobs.steps.helpers.GroupNamesHolder
import jobs.steps.helpers.MultiNumericClinicalVariableColumnConfigurator;
import jobs.steps.helpers.SysinflameMultiNumericClinicalVariableColumnConfigurator
import jobs.steps.helpers.SimpleAddColumnConfigurator
import jobs.steps.helpers.SysinflameResultInstanceIdsHolder;
import jobs.table.Table
import jobs.table.columns.ConstantValueColumn
import jobs.table.columns.PrimaryKeyColumn

import org.codehaus.groovy.grails.web.i18n.ParamsAwareLocaleChangeInterceptor;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

import static jobs.steps.AbstractDumpStep.DEFAULT_OUTPUT_FILE_NAME
/**
 *@Author Benjamin Baum, Department of Medical Informatics, G�ttingen, <benjamin.baum@med.uni-goettingen.de> 
 */
@Component
@Scope('job')
class SysinflameOutlier extends AbstractAnalysisJob {


	@Autowired
	SimpleAddColumnConfigurator primaryKeyColumnConfigurator

    @Autowired
    MultiNumericClinicalVariableColumnConfigurator columnConfigurator

	@Autowired
	Table table

	GroupNamesHolder holder = new GroupNamesHolder()

	@PostConstruct
	void init() {
		primaryKeyColumnConfigurator.column = new PrimaryKeyColumn(header: 'PATIENT_NUM')

		columnConfigurator.header = 'VALUE'
		columnConfigurator.keyForConceptPaths = 'variablesConceptPaths'
		columnConfigurator.groupNamesHolder = holder
	}


	@Override
	protected List<Step> prepareSteps() {

		List<Step> steps = []

		steps << new ParametersFileStep(
				temporaryDirectory: temporaryDirectory,
				params: params)

		steps << new BuildTableResultStep(
				table: table,
				configurators: [primaryKeyColumnConfigurator, columnConfigurator]) //categoryVariableConfigurator,categoryVariableConfigurator

		steps << new SysinflameSimpleDumpTableResultStep(
				table: table,
				temporaryDirectory: temporaryDirectory,
//				groupNamesHolder:   holder,
				outputFileName: DEFAULT_OUTPUT_FILE_NAME)
				println "TABLE HEADER " + table.getHeaders().toString();
		steps << new RCommandsStep(
				temporaryDirectory: temporaryDirectory,
				scriptsDirectory: scriptsDirectory,
				rStatements: RStatements,
				studyName: studyName,
				params: params,
				extraParams: [inputFileName: DEFAULT_OUTPUT_FILE_NAME, $chkGroupIndex:params.chkGroupIndex])

		steps
	}

	@Override
	protected List<String> getRStatements() {
		[
			'''source('$pluginDirectory/Sysinflame/Outlier/Outlier.R')''',
			'''Extremwerte.loader(
			 input.filename = '$inputFileName'
             )'''
		]
	}

	@Override
	protected getForwardPath() {
		"/sysinflameOutlier/sysinflameOutlierOutput?jobName=$name"
	}
}
