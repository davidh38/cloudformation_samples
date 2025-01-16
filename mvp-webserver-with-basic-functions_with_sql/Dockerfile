
# Use an official Clojure image as the base
FROM clojure:openjdk-17-tools-deps

# Set the working directory inside the container
WORKDIR /app

# Copy project files into the container
COPY deps.edn .
COPY src ./src
COPY resources ./resources

# Download dependencies (cached if deps.edn hasn't changed)
RUN clj -P

# Run the application
CMD ["clojure", "-M:run"]
