data <- read.csv("FastClassiferData.csv")
library(lattice)
with(data, xyplot(AvgY ~ AvgX, group=Symbol))
with(data, textxy(AvgX, AvgY, Symbol))
with(data, xyplot(AvgY ~ AvgX, group=Symbol, panel=function(x, y, ...) {
  panel.xyplot(AvgX, AvgY, ...);
  ltext(x=AvgX, y=AvgY, labels=Symbol, pos=1, offset=1, cex=0.8)
}))
