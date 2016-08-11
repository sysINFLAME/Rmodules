package jobs

import jobs.steps.*
import jobs.steps.helpers.CategoricalColumnConfigurator;
import jobs.steps.helpers.GroupNamesHolder
import jobs.steps.helpers.MultiNumericClinicalVariableColumnConfigurator
import jobs.steps.helpers.SimpleAddColumnConfigurator
import jobs.steps.helpers.SysinflameMultiCategoricalClinicalVariableColumnConfigurator
import jobs.steps.helpers.SysinflameMultiNumericClinicalVariableColumnConfigurator
import jobs.table.Table
import jobs.table.columns.PrimaryKeyColumn

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

import static jobs.steps.AbstractDumpStep.DEFAULT_OUTPUT_FILE_NAME


@Component
@Scope('job')
class Phenomap extends AbstractAnalysisJob {
    
	@Autowired
    SimpleAddColumnConfigurator primaryKeyColumnConfigurator

	@Autowired
	SysinflameMultiCategoricalClinicalVariableColumnConfigurator columnConfigurator2
	
	@Autowired
	CategoricalColumnConfigurator constantConfigurator
    @Autowired
    Table table

    GroupNamesHolder holder = new GroupNamesHolder()

    @PostConstruct
    void init() {
		
    	primaryKeyColumnConfigurator.column = new PrimaryKeyColumn(header: 'PATIENT_NUM')

		
		
		columnConfigurator2.header = 'CATEGORY'
		columnConfigurator2.keyForConceptPaths = 'variablesConceptPaths'
//		
//		constantConfigurator.header = 'CONSTANT'
//		constantConfigurator.keyForConceptPaths=''
		
//		columnConfigurator2.groupNamesHolder = holder
    }
	

    @Override
    protected List<Step> prepareSteps() {

        List<Step> steps = []

        steps << new ParametersFileStep(
                temporaryDirectory: temporaryDirectory,
                params: params)

        steps << new BuildTableResultStep(
                table: table,
                configurators: [primaryKeyColumnConfigurator,columnConfigurator2]) //categoryVariableConfigurator,categoryVariableConfigurator
//
     
		//MultiRowAsGroupDumpTableResultsStep 
		//SimpleDumpTableResultStep
		steps << new SysinflameSimpleDumpTableResultStep(
                table: table,
                temporaryDirectory: temporaryDirectory,
                outputFileName: DEFAULT_OUTPUT_FILE_NAME)
        
        steps << new RCommandsStep(
                temporaryDirectory: temporaryDirectory,
                scriptsDirectory: scriptsDirectory,
                rStatements: RStatements,
                studyName: studyName,
                params: params,
                extraParams: [inputFileName: DEFAULT_OUTPUT_FILE_NAME])

        steps
    }

	
    
    @Override
    protected List<String> getRStatements() {
	log.warn('GETRGETR')
	log.warn("chkGroupIndexchkGroupIndexchkGroupIndex: " + params.chkGroupIndex)
        [   
			'''source('$pluginDirectory/Phenomap/PhenomapLoader.R')''',
			 '''Phenomap.loader(
             input.filename = '$inputFileName'
             )'''
        ]
    }

    @Override
    protected getForwardPath() {
	log.warn('FORWARDPATH')
    	 "/sysinflameDiversity/sysinflameDiversityOutput?jobName=$name"
    }
}
