from flask import Flask
from Adafruit_PWM_Servo_Driver import PWM
import math
import subprocess

app = Flask(__name__)

pwm = PWM(0x40)

servoMin = 150
servoMax = 600
half_pi = math.pi / 2.0
midpoint = 375.0
span = 225.0

def init():
    pwm.setPWMFreq(60)
    pwm.setPWM(0, 0, 375)
    pwm.setPWM(0, 0, 375)


@app.route('/')
def hello():
    return "sawP:"

def normalize(num):
    return int(midpoint + ((num / half_pi) * span))

@app.route("/move/<pitch>/<yaw>")
def move(pitch, yaw):
    pwm.setPWM(0, 0, normalize(-float(pitch)))
    pwm.setPWM(3, 0, normalize(float(yaw)))
    print "moved the motors"
    return "OK"

if __name__ == '__main__':
    init()
    app.run(host='0.0.0.0', port=8080, debug=True, use_reloader=False)
