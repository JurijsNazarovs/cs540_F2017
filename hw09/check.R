data <- read.table("/Users/owner/Box Sync/UW/cs540/hw/hw09/dataOut.txt",
                   sep = " ", header = F, stringsAsFactors = F)

data <- data.frame(x = data$V1, y = data$V2)

beta0 <- 300
beta1 <- -0.1
mse <- 1/length(data$y)*sum((beta0 + beta1*data$x - data$y)^2)

model <- lm(y ~ 1 + x, data)
model$coefficients