######
#R- Code zur Erkennung von Ausreiﬂern zur Implementierung in TranSMART
#
#Carolin Knecht (2015)
#
#Weiterabgabe an Dritte bitte nur nach Absprache!
######

Extremwerte.loader <- function(
  input.filename,
  output.file="Extremwerte",
  output.name="Extremwerte"
)
{
#benˆtigte Pakete
library(Cairo)
library(gridExtra)
library(outliers) 
library(calibrate)
#Daten werten eingelesen
input<-read.delim(input.filename,header=T)


Spalten <- dim(input)[2]

Ausgabe <- data.frame(Spaltenname="",Samples="",Extremwerte="",Methode="",Bemerkung="")

for(l in 2:Spalten)
{  
Werte <- input[,l]
sortiert <- sort(Werte)
Sample <- input[,1]
Eingabe <- data.frame(Sample,Werte)
Anzahl <- length(Werte)

#Je nachdem wieviele Werte vorliegen, werden unterschiedliche Tests durchgef¸hrt
if(Anzahl<3)
{  Methode <- "Nicht_sinnvoll"}
if(Anzahl>=3 && Anzahl <= 8)
{  Methode <- "Dixon"}
if(Anzahl>8 && Anzahl <= 25)
{  Methode <- "Grubbs"}
if(Anzahl>=25)
{ Methode <- "Standard_Extemwerte"}


switch(Methode, 
       Grubbs={
         pWert <-0
         #In jedem Durchlauf werden die Ausreiﬂer rausgeschmissen, bis keine mehr drin sind
         while(pWert<=0.05 && sum(sortiert)!=0 )
         {  
           GrubbsTest <- grubbs.test(sortiert)
           pWert <- GrubbsTest$p.value
           if(pWert<=0.05&& sum(sortiert)!=0)
           {
             GrubbsTest.Ausreiﬂer <- GrubbsTest$alternative
             Wert <- strsplit(GrubbsTest.Ausreiﬂer," ")[[1]]
             Ausreiﬂer <- Wert[3]
             sortiert <- sortiert[!sortiert==as.numeric(Ausreiﬂer)] 
             }
            }
         if(sum(sortiert)==0)
         {
           Bemerkung <- "Achtung, nur noch Nullen in den Daten!" 
         } else
         {
           Bemerkung <-""
         }
       },
      	
       Dixon={
         pWert <-0
         i <- 0
         while(pWert<=0.05  && sum(sortiert)!=0 )
         {  
           DixonTest <- dixon.test(sortiert)
           pWert <- DixonTest$p.value
           i <- i+1
           if(pWert<=0.05  && sum(sortiert)!=0 )
           {
             DixonTest.Ausreiﬂer <- DixonTest$alternative
             Wert <- strsplit(DixonTest.Ausreiﬂer," ")[[1]]
             Ausreiﬂer <- Wert[3]
             sortiert <- sortiert[!sortiert==as.numeric(Ausreiﬂer)] 
           }
           
         }
         if(sum(sortiert)==0)
         {
           Bemerkung <- "Achtung, nur noch Nullen in den Daten!" 
         }         else
         {
           Bemerkung <-""
         }
       },
       Standard_Extemwerte={
         Median <- median(sortiert)
         Median.Deviation <- mad(sortiert,constant=1)
         Ausreiﬂer <- (sortiert < Median - 5.2*Median.Deviation) | (sortiert > Median + 5.2*Median.Deviation)
         Ausreiﬂer2 <- sortiert[Ausreiﬂer]
         sortiert <- sortiert[!sortiert==as.numeric(Ausreiﬂer2)] 
         Methode <- "Standardisierte Extremwertabweichung"
         if(sum(sortiert)==0)
         {
           Bemerkung <- "Achtung, nur noch Nullen in den Daten!" 
         }         else
         {
           Bemerkung <-""
         }
         },
       Nicht_sinnvoll={
         Methode <- "Keine Anwendung"
         Bemerkung <-"Keine sinnvolle Auswertung mˆglich"
       },
       {
        Median <- median(sortiert)
        Median.Deviation <- mad(sortiert,constant=1)
        Ausreiﬂer <- (sortiert < Median - 5.2*Median.Deviation) | (sortiert > Median + 5.2*Median.Deviation)
        Ausreiﬂer2 <- sortiert[Ausreiﬂer]
        sortiert <- sortiert[!sortiert==as.numeric(Ausreiﬂer2)] 
        Methode <- "Standardisierte Extremwertabweichung"
        if(sum(sortiert)==0)
        {
          Bemerkung <- "Achtung, nur noch Nullen in den Daten!" 
        }        else
        {
          Bemerkung <-""
        }
       }
)

#Extremwerte werden bestimmt
welche.extremwerte <- which(is.element(Werte,sortiert))
extremwerte<-Werte[-welche.extremwerte]

Extremwerte<- paste(extremwerte,collapse=",")
laenge <- length(extremwerte)

if(laenge==0)
{
  Extremwerte = "Keine Ausreisser"
}

Spaltenname <-colnames(input[l])
Patienten <- 1:17

#Hier kommt der Plotaufruf
if(laenge!=0)
{
Samples <- Eingabe[Eingabe$Werte  %in% extremwerte,c("Sample")]
Samples <- paste(Samples,collapse=",")
Sampleplot <-Eingabe[Eingabe$Werte  %in% extremwerte,c("Sample")] 

CairoPNG(file=paste("Bild",Spaltenname,".png",sep=""), width=800, height=400,units = "px")  
welche.Werte_ohneExtrem <- which(is.element(Werte,extremwerte))
Werte_ohneExtrem<-Werte[-welche.Werte_ohneExtrem]
stripchart(Werte,cex=2 ,pch=16,col="white",method="jitter", xlab=Spaltenname, cex.lab=1.5)
stripchart(Werte_ohneExtrem,cex=2 ,pch=16,col="steelblue",method="jitter", add=T)
stripchart(extremwerte, cex=2, pch=16,method="stack", col="darkred", add=T)
n <- 1.05
p <- (range(Werte)[2]-range(Werte)[1])/15
for(i in 1:length(extremwerte))
{
textxy(extremwerte[i]-p, n,Sampleplot[i], cx=0.8)
n <- n-0.05
}
dev.off()

} else 
{
  Samples <- ""
  CairoPNG(file=paste("Bild",Spaltenname,".png",sep=""), width=800, height=400,units = "px")  
  stripchart(Werte,cex=2 ,pch=16,col="steelblue",method="jitter", xlab=Spaltenname, cex.lab=1.5)
  dev.off()
}



Ausgabe_einzel <- data.frame(Spaltenname,Samples,Extremwerte,Methode,Bemerkung)
Ausgabe <- rbind(Ausgabe,Ausgabe_einzel)
#Hier wird das ganze Ausgegeben
}
Ausgabe <- Ausgabe[-1,]
rownames(Ausgabe) <- Ausgabe$Spaltenname
write.table(Ausgabe,file="Ausgabe.csv", sep=";", dec=",", row.names=F,quote=F)
Ausgabe <- Ausgabe[,-1]
pdf("Ausgabe.pdf", 10,Spalten-4)
grid.table(Ausgabe)
dev.off()
}
