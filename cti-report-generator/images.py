"""
images.py — Generate section banner images for CTI Digest
Usage: python3 images.py [--out-dir OUTPUT_DIR]
"""
import math, random, os, argparse
from PIL import Image, ImageDraw, ImageFont

FONT_BOLD   = '/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf'
FONT_NORMAL = '/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf'
FONT_MONO   = '/usr/share/fonts/truetype/dejavu/DejaVuSansMono.ttf'

W, H = 1400, 260
W_COVER, H_COVER = 1400, 820

def hex2rgb(h):
    h = h.lstrip('#')
    return tuple(int(h[i:i+2], 16) for i in (0, 2, 4))

def font(path, size):
    try:    return ImageFont.truetype(path, size)
    except: return ImageFont.load_default()

def gradient_bg(w, h, top, bot):
    img = Image.new('RGBA', (w, h))
    for y in range(h):
        t = y / h
        r = int(top[0] + (bot[0]-top[0])*t)
        g = int(top[1] + (bot[1]-top[1])*t)
        b = int(top[2] + (bot[2]-top[2])*t)
        for x in range(w):
            img.putpixel((x,y), (r,g,b,255))
    return img

def draw_circuit(draw, w, h, seed, color, n=20):
    random.seed(seed)
    for _ in range(n):
        x,y = random.randint(0,w), random.randint(0,h)
        for __ in range(random.randint(3,8)):
            d = random.choice(['h','v'])
            length = random.randint(30,150)
            nx = x + length*random.choice([-1,1]) if d=='h' else x
            ny = y if d=='h' else y + length*random.choice([-1,1])
            draw.line([(x,y),(nx,ny)], fill=color, width=2)
            if random.random()>0.5:
                draw.ellipse([(nx-4,ny-4),(nx+4,ny+4)], fill=color)
            x,y = nx,ny

def draw_hex_grid(draw, w, h, color, size=35):
    for row in range(0, h//size+2):
        for col in range(0, w//size+2):
            cx = col*size*1.73; cy = row*size*2
            if col%2==1: cy += size
            pts = [(cx+size*0.9*math.cos(math.radians(60*i-30)),
                    cy+size*0.9*math.sin(math.radians(60*i-30))) for i in range(6)]
            draw.polygon(pts, outline=color, fill=None)

def draw_nodes(draw, w, h, seed, color, n=15):
    random.seed(seed)
    nodes = [(random.randint(50,w-50), random.randint(20,h-20)) for _ in range(n)]
    for i,(x1,y1) in enumerate(nodes):
        for j,(x2,y2) in enumerate(nodes):
            if i<j and math.dist((x1,y1),(x2,y2))<200:
                draw.line([(x1,y1),(x2,y2)], fill=color, width=1)
    for (x,y) in nodes:
        r = random.randint(3,8)
        draw.ellipse([(x-r,y-r),(x+r,y+r)], fill=(0,180,255,120))

def draw_binary(draw, w, h, seed, color):
    random.seed(seed)
    f = font(FONT_MONO, 11)
    for x in range(0,w,14):
        for y in range(0,h,14):
            if random.random()>0.6:
                draw.text((x,y), str(random.randint(0,1)), font=f, fill=color)

def draw_scan(draw, w, h, color):
    for y in range(0,h,4):
        draw.line([(0,y),(w,y)], fill=color, width=1)

def draw_glitch(draw, w, h, seed, color, n=8):
    random.seed(seed)
    for _ in range(n):
        y=random.randint(0,h); bh=random.randint(2,8); off=random.randint(-30,30)
        draw.rectangle([(off,y),(w+off,y+bh)], fill=color)

def glow_text(draw, pos, text, f, fill, shadow=(0,0,0,180), offset=3):
    draw.text((pos[0]+offset,pos[1]+offset), text, font=f, fill=shadow)
    draw.text(pos, text, font=f, fill=fill)

# ── Icon drawing functions ────────────────────────────────────────────────────
def icon_lock(draw, w, h, c):
    cx,cy = w-130, h//2; r=38
    draw.arc([(cx-r,cy-r-20),(cx+r,cy+r-20)], 180, 0, fill=(*c,160), width=8)
    draw.rounded_rectangle([(cx-r,cy-22),(cx+r,cy+r)], radius=10, fill=(*c,80), outline=(*c,180), width=3)
    draw.ellipse([(cx-8,cy+2),(cx+8,cy+18)], fill=(*c,200))
    draw.polygon([(cx,cy+16),(cx-5,cy+32),(cx+5,cy+32)], fill=(*c,200))

def icon_bug(draw, w, h, c):
    cx,cy = w-130, h//2
    draw.ellipse([(cx-22,cy-28),(cx+22,cy+28)], fill=(*c,70), outline=(*c,180), width=3)
    draw.ellipse([(cx-14,cy-42),(cx+14,cy-18)], fill=(*c,70), outline=(*c,180), width=3)
    for i,a in enumerate([-60,-20,20]):
        rad=math.radians(a)
        draw.line([(cx-22,cy-10+i*16),(cx-62,cy-10+i*16-25*math.sin(rad))], fill=(*c,160), width=3)
        draw.line([(cx+22,cy-10+i*16),(cx+62,cy-10+i*16-25*math.sin(rad))], fill=(*c,160), width=3)
    draw.line([(cx-8,cy-42),(cx-25,cy-65)], fill=(*c,160), width=3)
    draw.line([(cx+8,cy-42),(cx+25,cy-65)], fill=(*c,160), width=3)
    draw.ellipse([(cx-28,cy-70),(cx-20,cy-62)], fill=(*c,200))
    draw.ellipse([(cx+20,cy-70),(cx+28,cy-62)], fill=(*c,200))

def icon_target(draw, w, h, c):
    cx,cy = w-130, h//2
    for r in [55,38,22,8]:
        draw.ellipse([(cx-r,cy-r),(cx+r,cy+r)], outline=(*c,160), width=3)
    draw.line([(cx-65,cy),(cx+65,cy)], fill=(*c,140), width=2)
    draw.line([(cx,cy-65),(cx,cy+65)], fill=(*c,140), width=2)
    draw.ellipse([(cx-5,cy-5),(cx+5,cy+5)], fill=(*c,220))

def icon_breach(draw, w, h, c):
    cx,cy = w-130, h//2-8
    pts=[(cx,cy-50),(cx+38,cy-30),(cx+38,cy+8),(cx,cy+50),(cx-38,cy+8),(cx-38,cy-30)]
    draw.polygon(pts, fill=(*c,50), outline=(*c,180))
    draw.line([(cx-5,cy-50),(cx+15,cy-10),(cx-8,cy+20),(cx+10,cy+50)], fill=(255,60,60,200), width=4)
    draw.rectangle([(cx+18,cy-18),(cx+26,cy+10)], fill=(255,60,60,180))
    draw.ellipse([(cx+17,cy+16),(cx+27,cy+26)], fill=(255,60,60,180))

def icon_ioc(draw, w, h, c):
    cx,cy = w-130, h//2
    nodes=[(cx,cy),(cx-50,cy-30),(cx+45,cy-35),(cx+50,cy+25),(cx-45,cy+30),(cx-20,cy+55)]
    for (x1,y1) in nodes:
        for (x2,y2) in nodes:
            if (x1,y1)!=(x2,y2) and math.dist((x1,y1),(x2,y2))<80:
                draw.line([(x1,y1),(x2,y2)], fill=(*c,80), width=2)
    for i,(nx,ny) in enumerate(nodes):
        r=12 if i==0 else 7
        draw.ellipse([(nx-r,ny-r),(nx+r,ny+r)], fill=(*c,180), outline=(255,255,255,100))

def icon_actor(draw, w, h, c):
    cx,cy = w-130, h//2
    draw.ellipse([(cx-25,cy-55),(cx+25,cy+5)], fill=(*c,100), outline=(*c,180), width=3)
    draw.ellipse([(cx-48,cy+10),(cx+48,cy+80)], fill=(*c,80), outline=(*c,160), width=3)
    for r in [60,75]:
        draw.arc([(cx-r,cy-r),(cx+r,cy+r)], 200, 340, fill=(*c,100), width=2)

def icon_checklist(draw, w, h, c):
    cx,cy = w-155, h//2-30; f=font(FONT_BOLD, 13)
    items = [((200,40,40,220),'CRITICAL'),((220,120,0,220),'HIGH'),((200,180,0,200),'MEDIUM')]
    for i,(ic_color,label) in enumerate(items):
        y=cy+i*32
        draw.rounded_rectangle([(cx,y),(cx+16,y+16)], radius=4, fill=ic_color)
        draw.text((cx+5,y+2),'✓',font=f,fill=(255,255,255,255))
        draw.text((cx+26,y+1),label,font=f,fill=(*c,200))
        draw.rectangle([(cx+26,y+20),(cx+26+50+i*15,y+21)],fill=(*c,100))

SECTION_CONFIGS = {
    'cover': None,
    'vuln':      dict(num='01', title='Vulnerabilities & Exploit Alert',
                      subtitle='CVEs actively exploited in the wild — patch prioritization required',
                      accent=(0,160,255), bg_top=(5,10,28), bg_bot=(10,22,55),
                      pattern='circuit', icon=icon_lock),
    'malware':   dict(num='02', title='Emerging Malware Notification',
                      subtitle='New malware families and stealers in active distribution campaigns',
                      accent=(200,50,50), bg_top=(20,5,8), bg_bot=(40,10,15),
                      pattern='binary', icon=icon_bug),
    'campaigns': dict(num='03', title='Active Campaigns & Threat Groups',
                      subtitle='Ongoing attack campaigns and threat actor forewarning',
                      accent=(255,140,0), bg_top=(22,12,4), bg_bot=(35,20,8),
                      pattern='hex', icon=icon_target),
    'breaches':  dict(num='04', title='Breaches Notification',
                      subtitle='Active breach campaigns and global security incidents',
                      accent=(180,30,200), bg_top=(18,5,22), bg_bot=(30,10,40),
                      pattern='glitch', icon=icon_breach),
    'ioc':       dict(num='05', title='Indicators of Compromise',
                      subtitle='Network and host-based indicators — feed to SIEM, firewall, DNS RPZ, EDR',
                      accent=(0,220,160), bg_top=(5,20,18), bg_bot=(8,35,30),
                      pattern='nodes', icon=icon_ioc),
    'actors':    dict(num='06', title='Threat Actor Profiles',
                      subtitle='Key threat groups with background, targeting context, and TTPs',
                      accent=(120,80,220), bg_top=(12,8,25), bg_bot=(20,14,40),
                      pattern='circuit', icon=icon_actor),
    'actions':   dict(num='07', title='Consolidated Action Items',
                      subtitle='Prioritized remediation actions — sorted by urgency',
                      accent=(0,200,100), bg_top=(5,20,10), bg_bot=(8,35,18),
                      pattern='hex', icon=icon_checklist),
}

def make_cover(out_dir, org='Nokia Networks', date='16 February 2026'):
    img = gradient_bg(W_COVER, H_COVER, (4,8,20), (8,20,45))
    overlay = Image.new('RGBA', (W_COVER,H_COVER), (0,0,0,0))
    draw = ImageDraw.Draw(overlay)
    draw_hex_grid(draw, W_COVER, H_COVER, (0,80,180,18), 50)
    draw_circuit(draw, W_COVER, H_COVER, 77, (0,140,255,50), 35)
    draw_binary(draw, W_COVER, H_COVER, 33, (0,255,100,18))
    draw_scan(draw, W_COVER, H_COVER, (0,200,255,10))
    draw_nodes(draw, W_COVER, H_COVER, 5, (0,100,200,60), 22)
    img = Image.alpha_composite(img, overlay)
    draw = ImageDraw.Draw(img)

    draw.rectangle([(0,0),(W_COVER,70)], fill=(0,10,30,255))
    draw.rectangle([(0,68),(W_COVER,72)], fill=(0,160,255,255))

    f_small = font(FONT_NORMAL,14); f_med=font(FONT_BOLD,18)
    f_xxl=font(FONT_BOLD,90); f_sub=font(FONT_BOLD,20)

    draw.text((20,22), f'{org}  |  Security Intelligence Center', font=f_small, fill=(150,200,255,200))
    draw.text((W_COVER-360,22), f'ISSUE: {date[:4]}-{date[-4:]}  |  TLP: AMBER', font=f_small, fill=(255,180,0,200))
    draw.rectangle([(60,90),(W_COVER-60,92)], fill=(0,160,255,200))
    draw.rectangle([(60,96),(W_COVER-60,97)], fill=(0,160,255,100))

    title='CYBER TELLIGENCE'
    bbox=f_xxl.getbbox(title); tw=bbox[2]-bbox[0]
    for off in range(6,0,-2):
        ga=30+off*8
        draw.text(((W_COVER-tw)//2-off, 102-off), title, font=f_xxl, fill=(0,120,255,ga))
        draw.text(((W_COVER-tw)//2+off, 102+off), title, font=f_xxl, fill=(0,120,255,ga))
    draw.text(((W_COVER-tw)//2, 100), title, font=f_xxl, fill=(255,255,255,255))

    subtitle='THREAT INTELLIGENCE REPORT  •  DAILY DIGEST'
    f_med2=font(FONT_BOLD,18); bbox2=f_med2.getbbox(subtitle); sw=bbox2[2]-bbox2[0]
    draw.text(((W_COVER-sw)//2, 202), subtitle, font=f_med2, fill=(0,200,255,220))
    draw.rectangle([(60,228),(W_COVER-60,230)], fill=(0,160,255,200))
    draw.rectangle([(60,233),(W_COVER-60,234)], fill=(0,160,255,80))

    col_w=(W_COVER-120)//3; col_gap=20; y_start=250
    for cx_i in [120+col_w+col_gap//2, 120+2*col_w+col_gap+col_gap//2]:
        draw.rectangle([(cx_i,y_start),(cx_i+1,H_COVER-60)], fill=(0,100,180,100))

    cols=[
        dict(label='  CRITICAL ALERT  ',lc=(180,20,20,240),
             headline='BEYONDTRUST\nRCE EXPLOITED\nIN <24 HOURS',
             body='Nation-state actors weaponized\nCVE-2026-1731 within hours of\nPoC release. PAM systems across\ntelecom NOCs at risk.\nImmediate patching required.',
             tag='VULNERABILITY'),
        dict(label='  HIGH SEVERITY  ',lc=(180,100,0,240),
             headline='OYSTERLOADER\nRHYSIDA LINK\nCONFIRMED',
             body='Multi-stage evasion loader\nlinked to Rhysida ransomware.\nTelecom critical infrastructure\nidentified as primary\ntarget sector.',
             tag='RANSOMWARE'),
        dict(label='  THREAT WATCH  ',lc=(0,100,180,240),
             headline='CLICKFIX WAVE\nDNS PAYLOAD\nVIA nslookup\nMODELORAT',
             body='New ClickFix delivers ModeloRAT\nvia DNS TXT records. Bypasses\nHTTP proxy inspection.\nDNS security controls\nurgently needed.',
             tag='CAMPAIGN'),
    ]
    f_pill=font(FONT_BOLD,11); f_col=font(FONT_BOLD,22); f_body=font(FONT_NORMAL,13); f_tag=font(FONT_MONO,11)
    for i,col in enumerate(cols):
        cx=60+i*(col_w+col_gap); cy=y_start+10
        bbox_p=f_pill.getbbox(col['label']); pw=bbox_p[2]-bbox_p[0]+20; ph=bbox_p[3]-bbox_p[1]+10
        draw.rounded_rectangle([(cx,cy),(cx+pw,cy+ph)], radius=8, fill=col['lc'])
        draw.text((cx+10,cy+5), col['label'], font=f_pill, fill=(255,255,255,255))
        cy+=ph+14
        for line in col['headline'].split('\n'):
            draw.text((cx,cy), line, font=f_col, fill=(255,255,255,245)); cy+=28
        cy+=8
        draw.rectangle([(cx,cy),(cx+col_w-20,cy+1)], fill=(0,160,255,150)); cy+=10
        for line in col['body'].split('\n'):
            draw.text((cx,cy), line, font=f_body, fill=(180,210,240,220)); cy+=18
        cy+=12
        draw.text((cx,cy), f'#{col["tag"]}', font=f_tag, fill=(0,180,255,180))

    draw.rectangle([(0,H_COVER-50),(W_COVER,H_COVER)], fill=(0,10,30,255))
    draw.rectangle([(0,H_COVER-52),(W_COVER,H_COVER-50)], fill=(0,160,255,255))
    draw.text((20,H_COVER-32), f'{org}  |  Security Intelligence Center  |  {date}', font=f_small, fill=(100,160,220,200))
    draw.text((W_COVER-420,H_COVER-32), 'CONFIDENTIAL — AUTHORIZED PERSONNEL ONLY', font=f_small, fill=(255,140,0,200))

    path=os.path.join(out_dir,'cover.png')
    img.convert('RGB').save(path)
    return path

def make_section_banner(out_dir, key, cfg):
    c = cfg['accent']; seed = int(cfg['num'])*7
    img = gradient_bg(W, H, cfg['bg_top'], cfg['bg_bot'])
    overlay = Image.new('RGBA',(W,H),(0,0,0,0))
    draw = ImageDraw.Draw(overlay)
    pat = cfg['pattern']
    if pat=='circuit':   draw_circuit(draw,W,H,seed,(*c,40),25); draw_hex_grid(draw,W,H,(*c,15),40)
    elif pat=='binary':  draw_binary(draw,W,H,seed*11,(*c,35)); draw_circuit(draw,W,H,seed*5,(*c,25),15)
    elif pat=='hex':     draw_hex_grid(draw,W,H,(*c,22),35); draw_circuit(draw,W,H,seed*13,(*c,30),15)
    elif pat=='nodes':   draw_nodes(draw,W,H,seed*3,(*c,60),20); draw_hex_grid(draw,W,H,(*c,12),50)
    elif pat=='glitch':  draw_hex_grid(draw,W,H,(*c,18),40); draw_glitch(draw,W,H,seed*9,(*c,50),8)
    draw_scan(draw,W,H,(*c,12))
    if cfg.get('icon'): cfg['icon'](draw,W,H,c)
    img = Image.alpha_composite(img, overlay)
    draw = ImageDraw.Draw(img)
    draw.rectangle([(0,0),(8,H)], fill=(*c,255))
    draw.rectangle([(0,H-4),(W,H)], fill=(*c,200))
    f_num=font(FONT_BOLD,72); f_title=font(FONT_BOLD,32); f_sub=font(FONT_NORMAL,14); f_pill=font(FONT_MONO,11)
    for off in range(4,0,-1):
        draw.text((30-off,25-off), cfg['num'], font=f_num, fill=(*c,30))
    draw.text((30,25), cfg['num'], font=f_num, fill=(*c,160))
    glow_text(draw,(140,38),cfg['title'].upper(),f_title,(255,255,255,255))
    draw.text((140,82), cfg['subtitle'], font=f_sub, fill=(*c,220))
    tag=f' SECTION {cfg["num"]} '
    bbox_t=f_pill.getbbox(tag); pw=bbox_t[2]-bbox_t[0]+16; ph=bbox_t[3]-bbox_t[1]+8
    draw.rounded_rectangle([(140,H-48),(140+pw,H-48+ph)], radius=6, fill=(*c,180))
    draw.text((148,H-44), tag, font=f_pill, fill=(255,255,255,255))
    cx2=W-120; cy2=H//2
    for r in range(80,10,-10):
        draw.ellipse([(cx2-r,cy2-r),(cx2+r,cy2+r)], fill=(*c,int(40*(80-r)/70)))
    path=os.path.join(out_dir,f'section_{key}.png')
    img.convert('RGB').save(path)
    return path

def generate_all(out_dir, org='Nokia Networks', date='16 February 2026'):
    os.makedirs(out_dir, exist_ok=True)
    paths={}
    paths['cover'] = make_cover(out_dir, org, date)
    for key,cfg in SECTION_CONFIGS.items():
        if key=='cover' or cfg is None: continue
        paths[key] = make_section_banner(out_dir, key, cfg)
    return paths

if __name__=='__main__':
    parser = argparse.ArgumentParser(description='Generate CTI report section images')
    parser.add_argument('--out-dir', default='./output/images', help='Output directory for images')
    parser.add_argument('--org', default='Nokia Networks', help='Organization name')
    parser.add_argument('--date', default='16 February 2026', help='Report date')
    args = parser.parse_args()
    paths = generate_all(args.out_dir, args.org, args.date)
    print(f'Generated {len(paths)} images in {args.out_dir}')
    for k,v in paths.items():
        print(f'  {k}: {v}')
