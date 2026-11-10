#!/usr/bin/env bash
# 反复尝试构建，直到 fabric maven 可达并且构建成功
set -u
cd "$(dirname "$0")/.."

export JAVA_HOME="/c/Program Files/Microsoft/jdk-17.0.10.7-hotspot"
GRADLE=".tools/gradle-8.5/bin/gradle"
PROBE="https://maven.fabricmc.net/net/fabricmc/fabric-loader/maven-metadata.xml"

for i in $(seq 1 20); do
    code=$(curl -s -o /dev/null -w "%{http_code}" --max-time 10 "$PROBE")
    echo "=== attempt $i: fabric maven = $code ===" >> .tools/build.log
    if [ "$code" = "200" ]; then
        "$GRADLE" build --stacktrace --console=plain >> .tools/build.log 2>&1
        rc=$?
        echo "=== gradle exit: $rc ===" >> .tools/build.log
        if [ $rc -eq 0 ]; then
            echo "BUILD_OK" >> .tools/build.log
            exit 0
        fi
    fi
    sleep 20
done
echo "BUILD_GAVE_UP" >> .tools/build.log
exit 1
