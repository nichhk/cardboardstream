from flask import Flask
from Adafruit_PWM_Servo_Driver import PWM
import math
import subprocess

app = Flask(__name__)

pwm = PWM(0x40)

# Constants defining the range of the servo
SERVO_MIN = 150
SERVO_MAX = 600
HALF_PI = math.pi / 2.0
MIDPOINT = 375.0
SPAN = 225.0

# channels
PAN = 3
TILT = 0

def init():
    pwm.setPWMFreq(60)
    pwm.setPWM(PAN, 0, 375)
    pwm.setPWM(TILT, 0, 375)


@app.route('/')
def hello():
    return "hello"


def normalize(num):
    '''
    num: a float between -PI and PI.
    returns: num normalized between SERVO_MIN and SERVO_MAX.
    '''
    return int(MIDPOINT + ((num / HALF_PI) * SPAN))

@app.route("/move/<pitch>/<yaw>")
def move(pitch, yaw):
    pwm.setPWM(TILT, 0, normalize(-float(pitch)))
    pwm.setPWM(PAN, 0, normalize(float(yaw)))
    return "OK"

if __name__ == '__main__':
    init()
    app.run(host='0.0.0.0', port=8080, debug=True, use_reloader=False)
