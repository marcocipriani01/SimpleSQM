#include <math.h>
#include <FreqMeasure.h>

#include "Config.h"

void setup() {
  Serial.begin(SERIAL_SPEED);
}

void loop() {
  if (Serial.available() && (Serial.read() == '>')) {
    double val = sqm();
    Serial.print("<");
    Serial.println(val);
  }
}

double sqm() {
    FreqMeasure.begin();
    int count = 0;
    double sum = 0.0;
    while (count < SQM_SAMPLES) {
        if (FreqMeasure.available()) {
            sum += FreqMeasure.read();
            count++;
            delay(SQM_AVG_DELAY);
        }
    }
    FreqMeasure.end();
    double frequency = F_CPU / (sum / count);
    return SQM_LIMIT - 2.5 * log10(frequency);
}
