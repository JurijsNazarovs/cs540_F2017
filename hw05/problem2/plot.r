dataRaw <- read.csv("ranked.list")

pdf("rawData.pdf")
plot(dataRaw$rank, dataRaw$count, xlab = "rank", ylab = "count",
     type = "l")
dev.off()

dataTmp <- log(dataRaw)/log(10)
pdf("logData.pdf")
plot(dataTmp$rank, dataTmp$count, xlab = "log(rank)", ylab = "log(count)",
     type = "p")
dev.off()

