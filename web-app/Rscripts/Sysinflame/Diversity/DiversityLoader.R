###########################################################################
#Correlation
#This will load our input files into variables so we can run the correlation.
###########################################################################

Diversity.loader <- function(
	input.filename,
	output.file="Diversity",
	output.name="Diversity"
)
{
	######################################################
	library(Cairo)
	######################################################
	
	#To be precise I take it this means the significance probability,
	#that is the chance of getting a value of the correlation as far
	#from zero in absolute value or more so as the one you got...
	
	
	######################################################
	#Read the correlation data.
	line.data<-read.delim(input.filename,header=T)
	
	#This is the name of the output file for the correlation.
	correlationResultsFile <- "Diversity.txt"
	write.table(line.data, file = "Diversity.txt", append = FALSE, quote = TRUE, sep = " ",
            eol = "\n", na = "NA", dec = ".", row.names = FALSE,
            col.names = TRUE, qmethod = c("escape", "double"),
            fileEncoding = "")
	
	
	CairoPNG(file=paste(output.name,".png",sep=""), width=800, height=800,units = "px")	

	plot(line.data)
	dev.off()
	######################################################
}
