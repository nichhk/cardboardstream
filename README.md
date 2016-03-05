# cardboardstream
Streams real-time video from a Raspberry Pi to a Google Cardboard VR Display (Android)

### Parts list
- [Raspberry Pi 2](http://www.amazon.com/Raspberry-Pi-Model-Project-Board/dp/B00T2U7R7I/ref=sr_1_3?s=pc&ie=UTF8&qid=1457136291&sr=1-3&keywords=raspberry+pi)
- [8GB MicroSD Card](http://www.amazon.com/dp/B00M55C0VU/ref=twister_B011BRUOMO?_encoding=UTF8&psc=1) loaded with [NOOBS](https://www.raspberrypi.org/help/noobs-setup/)
- [Micro USB power supply](http://www.amazon.com/EasyAcc-Charger-Portable-Samsung-External/dp/B00A9PO5AM/ref=sr_1_2?s=pc&ie=UTF8&qid=1457136345&sr=1-2&keywords=microusb+wall+plug)
- [Raspberry Pi Camera Module](http://www.amazon.com/Raspberry-5MP-Camera-Board-Module/dp/B00E1GGE40/ref=sr_1_1?s=pc&ie=UTF8&qid=1457136385&sr=1-1&keywords=raspberry+pi+camera)
- [Mini Pan-Tilt Kit](https://www.adafruit.com/products/1967)
- [16-Channel PWM HAT for Raspberry Pi](https://www.adafruit.com/products/2327)
- [Power supply for 16-Channel PWM HAT](https://www.adafruit.com/products/276)
- A good Android phone (I'm using a Moto X Pure)
- A Google Cardboard kit. I highly recommend [Mattel's plastic headset](http://www.amazon.com/View-Master-Virtual-Reality-Starter-Pack/dp/B011EG5HJ2/ref=sr_1_1?ie=UTF8&qid=1457208416&sr=8-1&keywords=mattel+vr). 

When I built this (January 2016), it cost about $150 in total, not including the phone.  

### Raspberry Pi Setup
1. Installing packages
  1. [uv4l](http://www.linux-projects.org/modules/sections/index.php?op=viewarticle&artid=14) to stream video from the RPi Camera
  ```
  $ sudo apt-get update
  $ sudo apt-get install uv4l uv4l-raspicam uv4l-server uv4l-webrtc
  ```
  2. Pip and Flask for the web server
  ```
  sudo apt-get install python-pip
  sudo pip install flask
  ```
  3. packages to enable i2c
  ```
  sudo apt-get install python-smbus
  sudo apt-get install i2c-tools
  ```
2. Follow [these instructions](https://learn.adafruit.com/adafruit-16-channel-pwm-servo-hat-for-raspberry-pi/overview) to set up the servos and pan-tilt kit.
  1. Attach the tilt servo to channel 0 and the pan servo to channel 3 (the channel choices can be configured in [server.py](https://github.com/nichhk/cardboardstream/blob/master/server/server.py).
  
### Run Instructions
1. Begin streaming the video. 
```
uv4l --driver raspicam --auto-video_nr --server-option '--port=5000' --encoding mjpeg --server-option '--enable-webrtc' --width 256 --height 288 --framerate 24 --vflip --hflip
```
This command will open up an MJPEG stream of the video on port 5000. You can specify the width, height, and framerate for the stream. I flipped the stream vertically and horizontally because of how my camera is attached to the pan-tilt module. 

2. Run the webserver.
```
cd /path/to/server
sudo python server.py
```
3. Open the Android app. 
  1. Enter the RPi's IP address. You can find it using `ifconfig`.
  2. The RPi and Android device must be on the same local network. 
4. Enjoy!
