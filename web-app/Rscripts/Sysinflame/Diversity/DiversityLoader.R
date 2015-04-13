Diversity.loader <- function(
	input.filename,
	output.file="Diversity",
	output.name="Diversity"
)
{
	######################################################
	library(Cairo)
	######################################################
	
	
	
	######################################################
	#Read the data.
	line.data<-read.delim(input.filename,header=T)
	
	
	CairoPNG(file=paste("Bild1",".png",sep=""), width=800, height=800,units = "px")	
	
	plot(line.data)
	dev.off()
	
	CairoPNG(file=paste("Bild2",".png",sep=""), width=800, height=800,units = "px")	
	plot(line.data)
	dev.off()
	CairoPNG(file=paste("Bild3",".png",sep=""), width=800, height=800,units = "px")
	plot(line.data)
	dev.off()
	######################################################
}
