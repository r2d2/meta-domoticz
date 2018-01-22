DESCRIPTION = "Domoticz scripts"
LICENSE = "CLOSED"
PR="r0"

# No information for SRC_URI yet (only an external source tree was specified)
SRC_URI = " \
	file://edfwifinfo.sh \
  file://edfwifinfo.cron \
	"
RDEPENDS_${PN} = " curl jq domoticz cronie"

inherit bin_package

INSANE_SKIP_${PN} += "already-stripped"

do_install() {
  install -d ${D}${sysconfdir}/domoticz
	install -d ${D}${sysconfdir}/cron.d

	install -m 0755 ${WORKDIR}/edfwifinfo.sh  ${D}${sysconfdir}/domoticz/
  install -m 0644 ${WORKDIR}/edfwifinfo.cron  ${D}${sysconfdir}/cron.d/

}
