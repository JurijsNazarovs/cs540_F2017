y <- c(3, 1, 1, 4, 2, 3, 4, 3)
z <- c(0.102, 0.223, 0.504, 0.493, 0.312, 0.508, 0.982, 0.887)
N <- length(y)
curPoint <- 2

f <- function(x){
  return (max(c(4 - abs(x), 2 - abs(x - 6), 2 - abs(x + 6)))) 
}

print("| --- | --- | --- | --- | --- | --- |")
print("| Iteration Number | y | z | curPoint | T | p |")
for(i in 1:N){
  T <- 2*(0.9)^i
  p <- exp(-abs(f(curPoint) - f(y[i]))/T) 
  print(paste0("| ", i, " | ", y[i], " | ", z[i],
               " | ", curPoint, " | ", round(T, 3), " | ", round(p, 3)))
  
  if (f(y[i]) > f(curPoint) || z[i] <= p){
    curPoint <- y[i]
  }
}

x <- seq(-11, 11, length.out = 100)
plot(x, sapply(x, f), type = "l")
  




## Just to play
for(i in 1:100){
  y <- seq(curPoint - 10, curPoint + 10, by = 1)
  y <- sample(y, 1)
  z <- runif(1);
  
  T <- 2*(0.9)^i
  p <- exp(-abs(f(curPoint) - f(y))/T) 
  #print(paste0("| ", i, " | ", y, " | ", z,
  #             " | ", curPoint, " | ", T, " | ", round(p, 3)))
  
  if (f(y) > f(x) || z <= p){
    curPoint <- y
  }
}
  

