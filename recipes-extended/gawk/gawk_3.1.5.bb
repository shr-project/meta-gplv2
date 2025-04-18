SUMMARY = "GNU awk text processing utility"
DESCRIPTION = "The GNU version of awk, a text processing utility. \
Awk interprets a special-purpose programming language to do \
quick and easy text pattern matching and reformatting jobs."
HOMEPAGE = "www.gnu.org/software/gawk"
BUGTRACKER  = "bug-gawk@gnu.org"
SECTION = "console/utils"

# gawk <= 3.1.5: GPLv2
# gawk >= 3.1.6: GPLv3
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552"

RDEPENDS:gawk += "gawk-common"
RDEPENDS:pgawk += "gawk-common"
PR = "r2"

SRC_URI = "\
    ${GNU_MIRROR}/gawk/gawk-${PV}.tar.gz \
    file://gawk-3.1.5_fix_for_automake-1.12.patch \
    file://0001-gawk-fix-non-glibc-gcc-4-compilation.patch \
"

SRC_URI[md5sum] = "4760325489479cac17fe0114b8f62f30"
SRC_URI[sha256sum] = "463dcb9d0ca398b1d4f5a332f6cd9cec56441265fca616f2ea1b44d459e9f0f8"

inherit autotools gettext texinfo update-alternatives

PACKAGES += "gawk-common pgawk"

FILES:${PN} = "${bindir}/gawk* ${bindir}/igawk"
FILES:gawk-common += "${datadir}/awk/* ${libexecdir}/awk/*"
FILES:pgawk = "${bindir}/pgawk*"

ALTERNATIVE:${PN} = "awk"
ALTERNATIVE_TARGET[awk] = "${bindir}/gawk"
ALTERNATIVE_PRIORITY = "100"

CFLAGS += "-D PROTOTYPES"

do_install:append() {
	# remove the link since we don't package it
	rm ${D}${bindir}/awk
}

# http://gecko.lge.com:8000/Errors/Details/1139831
# gawk-3.1.5/missing_d/strtod.c:39:15: error: conflicting types for 'atof'; have 'double(void)'
CFLAGS += "-std=gnu17"
