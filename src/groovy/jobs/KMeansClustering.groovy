package jobs

import au.com.bytecode.opencsv.CSVWriter
import org.transmartproject.core.dataquery.TabularResult
import org.transmartproject.core.dataquery.highdim.HighDimensionDataTypeResource
import org.transmartproject.core.dataquery.highdim.HighDimensionResource
import org.transmartproject.core.dataquery.highdim.assayconstraints.AssayConstraint
import org.transmartproject.core.dataquery.highdim.dataconstraints.DataConstraint
import org.transmartproject.core.dataquery.highdim.projections.Projection

class KMeansClustering extends AnalysisJob {

    @Override
    protected void runAnalysis() {
        updateStatus('Running KMeans analysis')

        String source = 'source(\'$pluginDirectory/Heatmap/KMeansHeatmap.R\')'

        // TODO What about clusters.number = 2, probes.aggregate = false?
        String createHeatmap = '''KMeansHeatmap.loader(
                            input.filename = \'outputfile\',
                            imageWidth     = as.integer(\'$txtImageWidth\'),
                            imageHeight    = as.integer(\'$txtImageHeight\'),
                            pointsize      = as.integer(\'$txtImagePointsize\'),
                            maxDrawNumber  = as.integer(\'$txtMaxDrawNumber\'))'''

        runRCommandList([source, createHeatmap])
    }

    @Override
    protected void writeData(Map<String, TabularResult> results) {
        withDefaultCsvWriter(results[AnalysisJob.SUBSET1]) { csvWriter ->
            csvWriter.writeNext(['PATIENT_NUM', 'VALUE', 'GROUP'] as String[])
            results[AnalysisJob.SUBSET1]?.rows?.each { row ->
                row.assayIndexMap.each { assay, index ->
                    csvWriter.writeNext(
                            ["${AnalysisJob.SUBSET1SHORT}_${assay.assay.patientInTrialId}", row.data[index], "${row.probe}_${row.geneSymbol}"] as String[]
                    )
                }
            }
            results[AnalysisJob.SUBSET2]?.rows.each { row ->
                row.assayIndexMap.each { assay, index ->
                    csvWriter.writeNext(
                            ["${AnalysisJob.SUBSET2SHORT}_${assay.assay.patientInTrialId}", row.data[index], "${row.probe}_${row.geneSymbol}"] as String[]
                    )
                }
            }
        }
    }

    @Override
    protected Map<String, TabularResult> fetchResults() {
        updateStatus('Gathering Data')

        [(AnalysisJob.SUBSET1) : fetchSubset(AnalysisJob.RESULTSET1), (AnalysisJob.RESULTSET2) : fetchSubset(AnalysisJob.SUBSET2)]
    }

    private TabularResult fetchSubset(String subset) {
        // only do this when filled
        if (jobDataMap[subset] != null) {
            HighDimensionDataTypeResource dataType = highDimensionResource.getSubResourceForType(
                    jobDataMap.divIndependentVariableType.toLowerCase()
            )
            List<AssayConstraint> assayConstraints = [
                    dataType.createAssayConstraint(
                            AssayConstraint.PATIENT_SET_CONSTRAINT, result_instance_id: jobDataMap[(subset==AnalysisJob.RESULTSET1)?AnalysisJob.RESULTSET1:AnalysisJob.RESULTSET2]
                    )
            ]
            assayConstraints.add(
                    dataType.createAssayConstraint(
                            AssayConstraint.ONTOLOGY_TERM_CONSTRAINT, concept_key: '\\\\Public Studies' + jobDataMap.variablesConceptPaths
                    )
            )

            List<DataConstraint> dataConstraints = [
                    dataType.createDataConstraint(
                            [keyword_ids: [jobDataMap.divIndependentVariablePathway]], DataConstraint.SEARCH_KEYWORD_IDS_CONSTRAINT
                    )
            ]

            Projection projection = dataType.createProjection([:], 'default_real_projection')

            // get the data
            return dataType.retrieveData(assayConstraints, dataConstraints, projection)
        }
    }

    @Override
    protected void renderOutput() {
        updateStatus('Completed', "/RKMeans/heatmapOut?jobName=${name}")
    }

    private HighDimensionResource getHighDimensionResource() {
        jobDataMap.grailsApplication.mainContext.getBean HighDimensionResource
    }
}
