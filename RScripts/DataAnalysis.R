data <- read.csv("FastClassiferData.csv")
library(lattice)
with(data, xyplot(AvgY ~ AvgX, group=Symbol))
with(data, textxy(AvgX, AvgY, Symbol))
with(data, xyplot(AvgY ~ AvgX, group=Symbol, panel=function(x, y, ...) {
  panel.xyplot(AvgX, AvgY, ...);
  ltext(x=AvgX, y=AvgY, labels=Symbol, pos=1, offset=1, cex=0.8)
}))


with(data, xyplot(Feature1Y ~ Feature2X, group=Symbol))
with(data, xyplot(Feature1Y ~ Feature1X, group=Symbol, panel=function(x, y, ...) {
  panel.xyplot(Feature1X, Feature1Y, ...);
  ltext(x=Feature1X, y=Feature1Y, labels=Symbol, pos=1, offset=1, cex=0.8)
}))
