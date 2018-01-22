# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
#
# NOTE: multiple licenses have been detected; they have been separated with &
# in the LICENSE value for now since it is a reasonable assumption that all
# of the licenses apply. If instead there is a choice between the multiple
# licenses then you should change the value to separate the licenses with |
# instead of &. If there is any doubt, check the accompanying documentation
# to determine which situation is applicable.
#
# The following license files were not able to be identified and are
# represented as "Unknown" below, you will need to check them yourself:
#   www/js/ace/LICENSE
#   MQTT/LICENSE.txt
#
LICENSE = "GPLv3 & Unknown & LGPLv2.1"
LIC_FILES_CHKSUM = "file://License.txt;md5=d32239bcb673463ab874e80d47fae504 \
                    file://www/js/ace/LICENSE;md5=794d11c5219c59c9efa2487c2b4066b2 \
                    file://MQTT/LICENSE.txt;md5=62ddc846179e908dc0c8efec4a42ef20 \
                    file://hardware/telldus-core/LICENSE;md5=4fbd65380cdd255951079008b364516c"

SRC_URI = " \
  git://github.com/domoticz/domoticz.git;protocol=https \
	file://domoticz.service   \
	"

# Modify these as desired
PV = "3.8153${SRCPV}"
SRCREV = "494fff71685f319b25e7824684c299162b19f8c3"

S = "${WORKDIR}/git"

# NOTE: unable to map the following pkg-config dependencies: libopenzwave (lua5.2 or lua or lua-5.2)
#       (this is based on recipes that have previously been built and packaged)
# NOTE: the following library dependencies are unknown, ignoring: usb libmosquittopp libopenzwave libudev md libexecinfo libtelldus-core
#       (this is based on recipes that have previously been built and packaged)
DEPENDS = "curl openssl boost zlib python3"
RDEPENDS_${PN} = " curl python3"

inherit cmake python-dir pkgconfig

# Specify any options you want to pass to cmake using EXTRA_OECMAKE:
EXTRA_OECMAKE = " -DCMAKE_INSTALL_PREFIX=/home/domoticz"

FILES_${PN} += "/home/domoticz/*"

inherit systemd

SYSTEMD_SERVICE_${PN} = "domoticz.service"

inherit useradd

USERADD_PACKAGES = "${PN}"

USERADD_PARAM_${PN} = "-u 1200 -d /home/domoticz -r -s /bin/sh -P 'domoticz' -g domoticz domoticz"

GROUPADD_PARAM_${PN} = "-g 880 domoticz"

do_install_append () {
  install -d ${D}${systemd_unitdir}/system/
  install -m 0644 ${WORKDIR}/domoticz.service  ${D}${systemd_unitdir}/system
	chown -R domoticz ${D}/home/domoticz
	chgrp -R domoticz ${D}/home/domoticz
}
