package jobs

import jobs.steps.*
import jobs.steps.helpers.CategoricalColumnConfigurator;
import jobs.steps.helpers.ColumnConfigurator
import jobs.steps.helpers.GroupNamesHolder
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
 *@Author Benjamin Baum, Department of Medical Informatics, Göttingen, <benjamin.baum@med.uni-goettingen.de> 
 */
@Component
@Scope('job')
class SysinflameDiversity extends AbstractAnalysisJob {


	@Autowired
	SimpleAddColumnConfigurator primaryKeyColumnConfigurator

	@Autowired
	SysinflameMultiNumericClinicalVariableColumnConfigurator columnConfigurator

	@Autowired
	CategoricalColumnConfigurator columnConfigurator2

	@Autowired
	Table table

	GroupNamesHolder holder = new GroupNamesHolder()

	@PostConstruct
	void init() {
		primaryKeyColumnConfigurator.column = new PrimaryKeyColumn(header: 'PATIENT_NUM')

		columnConfigurator.header = 'MICROBIOME'
		columnConfigurator.keyForConceptPaths = 'variablesMicrobiomConceptPaths'
		columnConfigurator.groupNamesHolder = holder

		columnConfigurator2.header = 'CATEGORY'
		columnConfigurator2.keyForConceptPaths = 'variablesCensorConceptPaths'

	}


	@Override
	protected List<Step> prepareSteps() {

		List<Step> steps = []

		steps << new ParametersFileStep(
				temporaryDirectory: temporaryDirectory,
				params: params)

		steps << new BuildTableResultStep(
				table: table,
				configurators: [primaryKeyColumnConfigurator,columnConfigurator,columnConfigurator2]) //categoryVariableConfigurator,categoryVariableConfigurator

		steps << new SimpleDumpTableResultStep(
				table: table,
				temporaryDirectory: temporaryDirectory,
				outputFileName: DEFAULT_OUTPUT_FILE_NAME)

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
			'''source('$pluginDirectory/Sysinflame/Diversity/DiversityLoader_MR.R')''',
			'''Diversity.loader(
             input.filename = '$inputFileName',
			 input.mode = '$chkGroupIndex'
             )'''
		]
	}

	@Override
	protected getForwardPath() {
		"/sysinflameDiversity/sysinflameDiversityOutput?jobName=$name"
	}
}
