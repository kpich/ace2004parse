#!/usr/bin/env python
''' For every .sgm file in the ACE 2004 corpus, outputs the gzipped XML giving
    the parse (dependency + constituent), according
    to Stanford CoreNLP. This outputs into the CWD, so you'll need to
    reorganize the output files later.
'''

import fnmatch
import optparse
import os
import re
import shlex
import subprocess
import sys

CORENLP_PATH = '/u/pichotta/lib/stanford-corenlp-full-2012-11-12'
CORENLP_CP = '%s:%s:%s:%s' % \
    (os.path.join(CORENLP_PATH, 'stanford-corenlp-1.3.4.jar'),
     os.path.join(CORENLP_PATH, 'stanford-corenlp-1.3.4-models.jar'),
     os.path.join(CORENLP_PATH, 'xom.jar'),
     os.path.join(CORENLP_PATH, 'joda-time.jar'))

def main(inbase):
  data_dir = os.path.join(os.path.join(inbase, 'data'), 'English')
  for dirname in os.listdir(data_dir):
    absdir = os.path.join(data_dir, dirname)
    if not os.path.isdir(absdir):
      continue
    for fname in os.listdir(absdir):
      if fnmatch.fnmatch(fname, '*.sgm'):
        print 'parsing %s...' % fname
        tmp_fname = make_tmp_file_and_get_name(os.path.join(absdir, fname))
        cmd = 'java -cp %s -Xmx3g edu.stanford.nlp.pipeline.StanfordCoreNLP \
               -annotators tokenize,ssplit,parse,lemma, -file %s' % \
              (CORENLP_CP, tmp_fname)
        subprocess.Popen(shlex.split(cmd)).communicate()
        print 'zipping %s.xml...' % tmp_fname
        cmd = 'gzip %s.xml' % tmp_fname
        subprocess.Popen(shlex.split(cmd)).communicate()

def make_tmp_file_and_get_name(absfname):
  tmp_fname = '%s.%s' % (os.path.basename(os.path.dirname(absfname)),
                         os.path.basename(absfname).replace('.sgm',
                                                            '.parse'))
  m = re.search(r'<TEXT>\s*(.*)</TEXT>', open(absfname, 'r').read(), re.DOTALL)
  assert m
  f = open(tmp_fname, 'w')
  # Now, we need to strip out any SGM tags sitting in the text, because
  # the ACE annotated data ignores them when computing offsets.
  f.write(re.sub(r'</*\w+>', '', m.group(1)))
  f.close()
  return tmp_fname

def get_parser():
  p = optparse.OptionParser()
  p.add_option("--base_of_ace", action="store", type="string",
               dest="base_of_ace", default=None,
               help="the base directory of the ACE 2004 data set. Required.")
  return p


if __name__ == '__main__':
  op = get_parser()
  (opts, args) = op.parse_args()
  if not (opts.base_of_ace):
    sys.stderr.write('More Options required. Call with --help for help.\n')
    sys.exit(1)
  main(opts.base_of_ace)
